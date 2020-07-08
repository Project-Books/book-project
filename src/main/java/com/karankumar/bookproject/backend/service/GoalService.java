/*
    The book project lets a user keep track of different books they've read, are currently reading or would like to read
    Copyright (C) 2020  Karan Kumar

    This program is free software: you can redistribute it and/or modify it under the terms of the
    GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
    warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with this program.
    If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.backend.entity.BaseEntity;
import com.karankumar.bookproject.backend.entity.ReadingGoal;
import com.karankumar.bookproject.backend.repository.GoalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * A Spring service that acts as the gateway to the @see GoalRepository -- to use the GoalRepository, you should go
 * via this GoalService.
 */
@Service
public class GoalService extends BaseService<ReadingGoal, Long> {

    private static final Logger LOGGER = Logger.getLogger(GoalService.class.getName());
    private GoalRepository goalRepository;

    public GoalService(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    @Override
    public ReadingGoal findById(Long id) {
        return goalRepository.getOne(id);
    }

    public List<ReadingGoal> findAll() {
        return goalRepository.findAll();
    }

    @Override
    public void save(ReadingGoal goal) {
        if (goal != null) {
            List<Long> matchingGoals = findAll().stream()
                .map(BaseEntity::getId)
                .filter(id -> id.equals(goal.getId())).collect(Collectors.toList());
            if (matchingGoals.size() == 1) {
                // if a goal with the same ID exists, set this incoming goal ID to null so that a new row in the
                // table is made rather than updating the row that has the same ID
                LOGGER.log(Level.INFO, "Matching goal IDs: " + matchingGoals);
                goal.removeId();
            }
            goalRepository.save(goal);
        }
    }

    @Override
    public void delete(ReadingGoal readingGoal) {
        goalRepository.delete(readingGoal);
    }

    public Long count() {
        return goalRepository.count();
    }

    @Override
    public void deleteAll() {
        goalRepository.deleteAll();
    }
}
