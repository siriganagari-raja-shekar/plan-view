package com.csye6220.esdfinalproject.service;

import com.csye6220.esdfinalproject.dao.CardDAO;
import com.csye6220.esdfinalproject.model.Card;
import com.csye6220.esdfinalproject.model.Comment;
import com.csye6220.esdfinalproject.model.IssueCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardDAO cardDAO;

    @Override
    public void saveCard(Card card) {
        cardDAO.saveCard(card);
    }

    @Override
    public void updateCard(Card card) {
        cardDAO.updateCard(card);
    }

    @Override
    public void deleteCard(Card card) {
        cardDAO.deleteCard(card);
    }

    @Override
    public Card getCardById(long id) {
        return cardDAO.getCardById(id);
    }

    @Override
    public List<Comment> getCommentsByCardId(long cardId) {
        return cardDAO.getCommentsByCardId(cardId);
    }

    @Override
    public List<IssueCard> getCardsAssignedToUser(long userId) {
        return  cardDAO.getCardsAssignedToUser(userId);
    }

    @Override
    public long getAllCardsCount() {
        return cardDAO.getAllCardsCount();
    }

}

