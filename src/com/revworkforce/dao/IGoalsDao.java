package com.revworkforce.dao;

import com.revworkforce.model.Goals;
import java.util.List;

public interface IGoalsDao {

    boolean addGoal(Goals goal);

    boolean updateGoalProgress(int goalId, int progress, String status);

    Goals getGoalById(int goalId);

    List<Goals> getGoalsByEmpId(int empId);

    boolean deleteGoal(int goalId);
}
