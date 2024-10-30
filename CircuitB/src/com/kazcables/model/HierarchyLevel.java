package com.kazcables.model;

public enum HierarchyLevel {
    EXECUTIVE(1),
    MANAGEMENT(2),
    SALES(3),
    MARKETING(4),
    ENGINEERING(5),
    INFORMATION_TECHNOLOGY(6);
    private final int level;

    HierarchyLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    // passes an int, gets hierarchy level
    public static HierarchyLevel valueOf(int level) {
        for (HierarchyLevel h_level : values()) {
            if (h_level.getLevel() == level) {
                return h_level;
            }
        }
        return null;
    }
    // USE CASE Role.HierarchyLevel hierarchyLevel = Role.HierarchyLevel.valueOf(hierarchyLevelInt);

    public int[] calculateSalary() {
        // [ MIN, MAX, INCREMENT ]
        switch (this.getLevel()) {
            case 1: // EXECUTIVE
                return new int[]{130000, 200000, 10000};
            case 2: // MANAGEMENT
                return new int[]{90000, 120000, 10000};
            case 3: // SALES
                return new int[]{40000, 80000, 10000};
            case 4: // MARKETING
                return new int[]{45000, 90000, 10000};
            case 5: // ENGINEERING
                return new int[]{60000, 100000, 10000};
            case 6: // I,T
                return new int[]{55000, 100000, 10000};
            default:
                return new int[]{};
        }
    }
}
