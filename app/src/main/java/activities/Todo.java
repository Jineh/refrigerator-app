package activities;
import java.io.Serializable;
public class Todo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String todoName;
    private boolean isSelected;

    public Todo(String todoName) {
        this.todoName = todoName;
    }


    public String getTodoName(){
        return todoName;
    }


    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
