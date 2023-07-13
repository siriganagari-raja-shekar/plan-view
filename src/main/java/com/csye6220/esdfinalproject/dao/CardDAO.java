package com.csye6220.esdfinalproject.dao;

import com.csye6220.esdfinalproject.model.Card;
import com.csye6220.esdfinalproject.model.Comment;
import com.csye6220.esdfinalproject.model.IssueCard;

import java.util.List;

public interface CardDAO {
    void saveCard(Card card);
    void updateCard(Card card);
    void deleteCard(Card card);
    Card getCardById(long id);
    List<Comment> getCommentsByCardId(long id);

    List<IssueCard> getCardsAssignedToUser(long userId);

    long getAllCardsCount();
}

