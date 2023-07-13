package com.csye6220.esdfinalproject.controller;


import com.csye6220.esdfinalproject.model.*;
import com.csye6220.esdfinalproject.service.CardService;
import com.csye6220.esdfinalproject.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class CardsController {


    @Autowired
    UserService userService;

    @Autowired
    CardService cardService;

    @PostMapping("/card/add")
    public String addCardToBoard(
            HttpServletRequest request,
            @RequestParam(name = "card-title", required = false) String title,
            @RequestParam(name = "card-description", required = false) String description,
            @RequestParam(name = "assigned-user", required = false) Long assignedUserId,
            @RequestParam(name = "card-priority", required = false) String priority,
            @RequestParam(name = "card-severity", required = false) String severity,
            @RequestParam(name = "card-text", required = false) String cardText,
            @RequestParam(name = "card-status") String status
    ){
        Board board = (Board) request.getAttribute("board");
        User createdUser = (User) request.getSession().getAttribute("user");


        Card card = null;

        if(board.getType().equals(BoardType.SPRINT_RETROSPECTIVE)){
            card = new NoteCard(status, createdUser, board, cardText, 0);
        }
        else{
            User assignedUser = userService.getUserById(assignedUserId);
            card = new IssueCard(status, createdUser, board, title, assignedUser, description, priority, severity);
        }
        cardService.saveCard(card);

        return "redirect:/boards/view/" + board.getId();
    }

    @PostMapping("/card/edit/{cardId}")
    public String editCard(
            HttpServletRequest request,
            @PathVariable(name = "cardId") Long cardId,
            @RequestParam(name = "card-title", required = false) String title,
            @RequestParam(name = "card-description", required = false) String description,
            @RequestParam(name = "assigned-user", required = false) Long assignedUserId,
            @RequestParam(name = "card-priority", required = false) String priority,
            @RequestParam(name = "card-severity", required = false) String severity,
            @RequestParam(name = "card-status", required = false) String status,
            @RequestParam(name = "card-text", required = false) String cardText,
            RedirectAttributes redirectAttributes
    ) {

        System.out.println();
        Board board = (Board)request.getAttribute("board");

        if(board.getType() == BoardType.SPRINT_RETROSPECTIVE){
            NoteCard noteCard = (NoteCard) cardService.getCardById(cardId);
            noteCard.setText(cardText);
            System.out.println("Set note card text to: " + noteCard.getText());
            cardService.updateCard(noteCard);
        }
        else{
            IssueCard issueCard = (IssueCard) cardService.getCardById(cardId);

            issueCard.setTitle(title);
            issueCard.setDescription(description);
            issueCard.setAssignedTo(userService.getUserById(assignedUserId));
            issueCard.setPriority(priority);
            issueCard.setSeverity(severity);

            if(board.getType() == BoardType.KANBAN){
                long limit = board.getColumns().get(1).getCardsLimit();
                long numberOfCardsInNewStatus = board.getCards().stream().filter(c -> c.getStatus().equals(status)).count();

                if(numberOfCardsInNewStatus == limit){
                    redirectAttributes.addAttribute("errorMessage", "Column limit reached. Cannot add more cards. Move cards before adding new ones");
                }
                else{
                    issueCard.setStatus(status);
                }
            }
            else{
                issueCard.setStatus(status);
            }
            cardService.updateCard(issueCard);
        }

        return "redirect:/boards/view/" + board.getId();
    }

    @GetMapping("/card/delete/{cardId}")
    public String deleteCard(
            HttpServletRequest request,
            @PathVariable(name = "cardId") Long cardId
    ){

        Card card = cardService.getCardById(cardId);

        String redirectLink = "redirect:/boards/view/" + card.getBoard().getId();
        cardService.deleteCard(card);

        return redirectLink;
    }


    @PostMapping("/card/{id}/comment/add")
    public String addComment(
            HttpServletRequest request,
            @PathVariable(name = "id") Long cardId,
            @RequestParam(name = "commentText") String commentText
    ){
        Card card = cardService.getCardById(cardId);

        if(card.getComments() == null)
            card.setComments(new ArrayList<>());

        Comment comment = new Comment((User) request.getSession().getAttribute("user"), commentText);

        card.getComments().add(comment);

        cardService.updateCard(card);

        return "redirect:/boards/view/" + card.getBoard().getId();
    }

    @GetMapping("/card/{id}/upvote")
    public String addUpvote(
            HttpServletRequest request,
            @PathVariable(name = "id") Long cardId
    ){
        NoteCard noteCard = (NoteCard) cardService.getCardById(cardId);
        User userInSession = (User) request.getSession().getAttribute("user");
        if(noteCard.getVotedBy() == null){
            noteCard.setVotedBy(new HashSet<>());
        }

        boolean userAlreadyVoted = noteCard.getVotedBy().stream().filter(u -> u.getId()==userInSession.getId()).findAny().isPresent();
        if(!userAlreadyVoted){
            noteCard.getVotedBy().add(userInSession);
            noteCard.setVotes(noteCard.getVotes()+1);
        }

        cardService.updateCard(noteCard);

        return "redirect:/boards/view/" + noteCard.getBoard().getId();
    }

    @GetMapping("/cards/{userId}/view")
    public ModelAndView viewCards(
            HttpServletRequest request,
            @PathVariable(name = "userId") Long userId,
            @RequestParam(name = "filter", required = false) String filter,
            @RequestParam(name = "sort-by", required = false) String sortBy
    ){

        User user = (User) request.getSession().getAttribute("user");

        if(user.getRole() == UserRole.USER && user.getId() != userId){
            return new ModelAndView("forbidden");
        }
        ModelAndView mv = new ModelAndView("view_cards");


        List<String> validFilters = List.of("all", "open", "closed");
        filter = filter.toLowerCase(Locale.ROOT).trim();
        filter = validFilters.contains(filter) ? filter : "all";


        mv.addObject("user", user);

        List<IssueCard> cards = cardService.getCardsAssignedToUser(user.getId());

        switch (filter){
            case "all":
                break;
            case "open":
                cards = cards.stream().filter(card -> {
                    String lastColumnInBoards = card.getBoard().getColumns().get(card.getBoard().getColumns().size()-1).getStatus();
                    return !card.getStatus().equals(lastColumnInBoards);
                }).collect(Collectors.toList());
                break;
            case "closed":
                cards = cards.stream().filter(card -> {
                    String lastColumnInBoards = card.getBoard().getColumns().get(card.getBoard().getColumns().size()-1).getStatus();
                    return card.getStatus().equals(lastColumnInBoards);
                }).collect(Collectors.toList());
                break;
            default:
                System.out.println("Invalid filter");;
        }

        Map<String, Comparator<IssueCard>> cardComparators = new HashMap<>();

        cardComparators.put("lastUpdated", Comparator.comparing(IssueCard::getLastUpdated));
        cardComparators.put("timeCreated", Comparator.comparing(IssueCard::getTimeCreated));
        cardComparators.put("priority", Comparator.comparing(IssueCard::getPriority));
        cardComparators.put("severity", Comparator.comparing(IssueCard::getSeverity));

        sortBy = cardComparators.containsKey(sortBy) ? sortBy : "lastUpdated";

        cards.sort(cardComparators.get(sortBy));

        mv.addObject("filter", filter);
        mv.addObject("sortBy", sortBy);
        mv.addObject("cards", cards);

        return mv;
    }
}
