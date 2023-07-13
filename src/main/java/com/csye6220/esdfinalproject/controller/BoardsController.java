package com.csye6220.esdfinalproject.controller;

import com.csye6220.esdfinalproject.model.*;
import com.csye6220.esdfinalproject.service.BoardService;
import com.csye6220.esdfinalproject.service.TeamService;
import com.csye6220.esdfinalproject.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/boards")
@Validated
public class BoardsController {


    @Autowired
    BoardService boardService;

    @Autowired
    UserService userService;

    @Autowired
    TeamService teamService;

    @GetMapping
    public ModelAndView showBoards(@RequestParam(required = false) String filter, @RequestParam(required = false) String query, HttpServletRequest request){

        filter = filter == null ? "ALL" : filter.toUpperCase(Locale.ROOT);
        query = query == null ? "" : query;

        User user = userService.getUserById(((User)request.getSession().getAttribute("user")).getId());

        System.out.println("No of boards created: " + user.getCreatedBoards().size());

        ModelAndView mv = new ModelAndView("boards");

        Set<String> validBoardTypes = Arrays.stream(BoardType.values()).map( b -> b.toString()).collect(Collectors.toSet());

        Set<Board> filteredBoards = user.getCreatedBoards();

        if(validBoardTypes.contains(filter)){
            BoardType boardType = BoardType.valueOf(filter);
            filteredBoards = user.getCreatedBoards().stream().filter(board -> board.getType().equals(boardType)).collect(Collectors.toSet());
        }
        else{
            filter = "ALL";
        }

        if(!query.isBlank()){
            final String tempQuery = query;
            filteredBoards = filteredBoards.stream().filter(board -> board.getName().toLowerCase().contains(tempQuery.toLowerCase())).collect(Collectors.toSet());
        }

        mv.addObject("filteredBoards", filteredBoards);
        mv.addObject("query", query);
        mv.addObject("filter", filter);
        return mv;
    }

    @GetMapping("/create")
    public ModelAndView navigateToCreateBoard(HttpServletRequest request){

        ModelAndView mv = new ModelAndView("create_board");

        User userInSession = (User) request.getSession().getAttribute("user");

        mv.addObject("user", userService.getUserById(userInSession.getId()));

        mv.addObject("allowedTeams", userService.getUserById(userInSession.getId()).getTeams());

        return mv;
    }

    @PostMapping("/add")
    public ModelAndView createNewBoard(HttpServletRequest request,
                                 @RequestParam(name = "board-name") @Size(min = 4, message = "Board name should be atleast 4 characters") String boardName,
                                 @RequestParam(name = "board-type") @NotNull(message = "Board type cannot be null") String boardType,
                                 @RequestParam(name = "column") @NotNull(message = "Columns cannot be null") String[] columns,
                                 @RequestParam(name = "team-id") @NotNull(message = "Team id cannot be null") Long teamId,
                                 @RequestParam(name = "cards-limit", required = false) @Min(value = 3, message = "Card limit should atleast be 3") Integer cardsLimit,
                                 @RequestParam(name = "sprint-start-date", required = false)  String startDate,
                                 @RequestParam(name = "sprint-end-date", required = false)  String endDate
                                 ){
        System.out.println("Board name: " + boardName);
        System.out.println("Board type: " + BoardType.valueOf(boardType));
        System.out.println("Columns: " + Arrays.toString(columns));
        System.out.println("Cards-limit: " + cardsLimit);
        System.out.println("Sprint start date: "+ startDate);
        System.out.println("Sprint end date: "+ endDate);


        LocalDateTime startDateAndTime = convertTime(startDate, "start"), endDateAndTime = convertTime(endDate, "end");

        if(startDateAndTime != null && endDateAndTime != null && (startDateAndTime.isBefore(LocalDateTime.now()) || endDateAndTime.isBefore(LocalDateTime.now())))
            throw new ConstraintViolationException("Start date and end date should be in future", null);


        BoardType boardTypeTemp = BoardType.valueOf(boardType);

        cardsLimit = cardsLimit == null ? Integer.MAX_VALUE : cardsLimit;

        Team team = teamService.getTeamById(teamId);

        User user = userService.getUserById(((User)request.getSession().getAttribute("user")).getId());

        Board board = new Board(boardName, boardTypeTemp, team, startDateAndTime, endDateAndTime, user);

        boardService.save(board);

        if(board.getColumns() == null)
            board.setColumns(new ArrayList<>());

        int i = 0;
        if(boardTypeTemp == BoardType.KANBAN || boardTypeTemp == BoardType.BACKLOG_REFINEMENT){
            board.getColumns().add(new Column("Backlog", 0, Integer.MAX_VALUE, board));
        }
        for (; i < columns.length; i++) {
            Column column = new Column(columns[i], boardTypeTemp == BoardType.KANBAN || boardTypeTemp == BoardType.BACKLOG_REFINEMENT ? i+1 : i, cardsLimit, board);
            board.getColumns().add(column);
        }


        boardService.update(board);

        ModelAndView mv = new ModelAndView("board_success");
        mv.addObject("board", board);

        return mv;
    }

    @GetMapping("/view/{id}")
    public ModelAndView viewBoard(HttpServletRequest request, @PathVariable(name = "id") Long id, @ModelAttribute(name = "errorMessage") String errorMessage){

        Board board = boardService.getBoardById(id);
        String viewName = board.getType().equals(BoardType.SPRINT_RETROSPECTIVE) ? "note_card_board" : "issue_card_board";

        if(checkUserValidityForBoardAccess(request, id) == false)
            return new ModelAndView("forbidden");

        ModelAndView mv = new ModelAndView(viewName);

        List<User> availableUsers = userService.getAllUsers();

        if(board.getCards() != null){
            board.getCards().forEach(card -> {
                if(card.getComments() != null && card.getComments().size()!= 0)
                    card.setComments(card.getComments().stream().distinct().collect(Collectors.toList()));
            });
        }

        if(errorMessage != null && !errorMessage.isBlank()){
            mv.addObject("errorMessage", errorMessage);
        }

        if(board.getType() == BoardType.KANBAN){
            Map<String, Integer> columnCardCount = new HashMap<>();
            board.getColumns().forEach(column -> columnCardCount.put(column.getStatus(), 0));
            if(board.getCards() != null)
                board.getCards().forEach(card -> columnCardCount.put(card.getStatus(), columnCardCount.get(card.getStatus())+1));
            mv.addObject("columnCardCount", columnCardCount);
        }
        boolean boardActive = true;
        if(board.getType() == BoardType.SPRINT){

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM, yyyy h:mm a");
            String boardStatusMessage = "Board is active only from " + board.getStartTime().format(formatter) + " to " + board.getEndTime().format(formatter) +" . All activity is disabled at this time";
            LocalDateTime now = LocalDateTime.now();
            if(now.isAfter(board.getStartTime()) && now.isBefore(board.getEndTime())){
                boardStatusMessage = "Boards is active now, till " + board.getEndTime().format(formatter) + ". You can make updates to the board";
            }
            else{
                boardActive = false;
            }
            mv.addObject("boardStatusMessage", boardStatusMessage);
        }
        mv.addObject("board", board);
        mv.addObject("boardActive", boardActive);
        mv.addObject("availableUsers", availableUsers);
        return mv;
    }

    @PostMapping("/{id}/card/add")
    public String addCardsToBoard(@PathVariable(name = "id") Long id, HttpServletRequest request){

        if(checkUserValidityForBoardAccess(request, id) == false)
            return "forbidden";

        Board board = boardService.getBoardById(id);

        request.setAttribute("board", board);

        return "forward:/card/add";
    }

    @PostMapping("/{id}/card/edit/{cardId}")
    public String editCard(@PathVariable(name = "id") Long id, @PathVariable(name = "cardId") Long cardId, HttpServletRequest request){

        if(checkUserValidityForBoardAccess(request, id) == false)
            return "forbidden";

        Board board = boardService.getBoardById(id);

        request.setAttribute("board", board);

        return "forward:/card/edit/" + cardId;
    }

    @GetMapping("/{id}/card/delete/{cardId}")
    public String deleteCard(@PathVariable(name = "id") Long id, @PathVariable(name = "cardId") Long cardId, HttpServletRequest request){

        if(checkUserValidityForBoardAccess(request, id) == false)
            return "forbidden";

        return "forward:/card/delete/" + cardId;
    }

    @PostMapping("/{id}/card/{cardId}/comment/add")
    public String addCommentToCard(@PathVariable(name = "id") Long id, @PathVariable(name = "cardId") Long cardId, HttpServletRequest request){

        if(checkUserValidityForBoardAccess(request, id) == false)
            return "forbidden";

        Board board = boardService.getBoardById(id);

        request.setAttribute("board", board);

        return "forward:/card/" + cardId + "/comment/add";
    }

    @GetMapping("/{id}/card/{cardId}/upvote")
    public String upvoteCard(@PathVariable(name = "id") Long id, @PathVariable(name = "cardId") Long cardId, HttpServletRequest request){

        if(checkUserValidityForBoardAccess(request, id) == false)
            return "forbidden";

        return "forward:/card/" + cardId + "/upvote";
    }
    private LocalDateTime convertTime(String date, String type){
        if(date == null || date.isEmpty() || date.isBlank())
            return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        LocalDate localDate = LocalDate.parse(date, formatter);
        LocalTime localTime = LocalTime.of( type.equals("start") ? 9 : 17,0);

        return LocalDateTime.of(localDate, localTime);
    }

    public boolean checkUserValidityForBoardAccess(HttpServletRequest request, long boardId){
        User user = (User) request.getSession().getAttribute("user");
        user = userService.getUserById(user.getId());
        boolean validAccess = true;
        if(user.getRole() == UserRole.USER){
             validAccess = user.getTeams()
                    .stream()
                    .map(team -> {
                        team = teamService.getTeamById(team.getId());
                        return team.getBoards();
                    })
                    .flatMap(Collection::stream)
                    .anyMatch(b -> b.getId().equals(boardId));
        }
        return validAccess;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ModelAndView constraintViolationException(ConstraintViolationException ex){
        Set<String> errors = new HashSet<>();
        if(ex.getConstraintViolations() == null){
            errors.add(ex.getMessage());
        }
        else{
            errors = ex.getConstraintViolations().stream().map(v -> v.getMessage()).collect(Collectors.toSet());
        }
        return new ModelAndView("error", "errors", errors);
    }
}
