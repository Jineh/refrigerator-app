package activities;

public class Food {
    String fName;
    int image;
    boolean isSelected;
    String docid;
    String time;

    public Food(String name, int img,boolean isS,String did){
        fName = name;
        image = img;
        isSelected = isS;
        docid = did;
    }
    public void setTime(String time){
        this.time = time;
    }

    public int getImage(){
        return image;
    }

    public String getName(){
        return fName;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}