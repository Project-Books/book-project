/*
 * The book project lets a user keep track of different books they would like to read, are currently
 * reading, have read or did not finish.
 * Copyright (C) 2020  Karan Kumar

 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.  See the GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.backend.entity.ReadingGoal;
import com.karankumar.bookproject.backend.repository.ReadingGoalRepository;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReadingGoalService {
    private final ReadingGoalRepository goalRepository;

    public ReadingGoalService(ReadingGoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    public ReadingGoal findById(Long id) {
        return goalRepository.getOne(id);
    }

    public List<ReadingGoal> findAll() {
        return goalRepository.findAll();
    }

    public void save(@NonNull ReadingGoal goal) {
        overwritePreviouslySetGoal(goal);
    }

    private void overwritePreviouslySetGoal(@NonNull ReadingGoal goal) {
        goalRepository.deleteAll();
        goalRepository.save(goal);
    }

    public void delete(@NonNull ReadingGoal readingGoal) {
        goalRepository.delete(readingGoal);
    }

    public void deleteAll() {
        goalRepository.deleteAll();
    }

    public Long count() {
        return goalRepository.count();
    }
}
