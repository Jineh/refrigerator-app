package activities;

public class Cook {
    private String cname;
    private String cnum;
    private String imageUrl;

    public Cook() {
        this.cname = cname;
        this.cnum = cnum;
        this.imageUrl = imageUrl;
    }

    public void setCName(String name){
        this.cname = name;
    }

    public void setCNum(String num){ this.cnum = num; }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public String getcName(){
        return cname;
    }

    public String getcNum(){ return cnum; }

    public String getImageUrl(){
        return imageUrl;
    }
}
