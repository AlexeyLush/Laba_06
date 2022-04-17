package commands;

import commands.models.CommandFields;

/**
 * Абстрактный класс команд
 */

public abstract class CommandAbstract {

    private String title;
    private String description;
    private String element;

    public abstract void execute(CommandFields commandFields);

    public String showInfoCommand(){
        return getDescription();
    }

    // Геттеры и сеттеры

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getElement(){
        return element;
    }

    public void setElement(String element){
        this.element = element;
    }

}
