package com.csye6220.esdfinalproject.service;

import com.csye6220.esdfinalproject.model.Card;
import com.csye6220.esdfinalproject.model.Comment;
import com.csye6220.esdfinalproject.model.IssueCard;

import java.util.List;

public interface CardService {
    void saveCard(Card card);
    void updateCard(Card card);
    void deleteCard(Card card);
    Card getCardById(long id);
    List<Comment> getCommentsByCardId(long cardId);
    List<IssueCard> getCardsAssignedToUser(long userId);

    long getAllCardsCount();
}

