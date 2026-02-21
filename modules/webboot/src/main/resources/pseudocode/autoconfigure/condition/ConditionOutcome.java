package com.boot.pseudocode.autoconfigure.condition;

public class ConditionOutcome
{
    private final boolean match;
    private final ConditionMessage message;

    public ConditionOutcome(boolean match, String message)
    {
        this(match, ConditionMessage.of(message, new Object[0]));
    }

    public ConditionOutcome(boolean match, ConditionMessage message)
    {
        this.match = match;
        this.message = message;
    }

    public static ConditionOutcome match()
    {
        return match(ConditionMessage.empty());
    }

    public static ConditionOutcome match(String message)
    {
        return new ConditionOutcome(true, message);
    }

    public static ConditionOutcome match(ConditionMessage message)
    {
        return new ConditionOutcome(true, message);
    }

    public static ConditionOutcome noMatch(String message)
    {
        return new ConditionOutcome(false, message);
    }

    public static ConditionOutcome noMatch(ConditionMessage message)
    {
        return new ConditionOutcome(false, message);
    }

    public boolean isMatch()
    {
        return this.match;
    }

    public String getMessage()
    {
        return this.message.isEmpty() ? null : this.message.toString();
    }

    public ConditionMessage getConditionMessage()
    {
        return this.message;
    }
    public static ConditionOutcome inverse(ConditionOutcome outcome)
    {
        return new ConditionOutcome(!outcome.isMatch(), outcome.getConditionMessage());
    }
}
