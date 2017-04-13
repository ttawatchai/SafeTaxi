package Modules;

/**
 * Created by ZIPPER on 4/10/2017.
 */

public class ListFriendStructur {
    String name;
    int image;

    public ListFriendStructur(String name,int image){
        this.name = name;
        this.image = image;
    }
    public String getName(){
        return name;
    }
    public int getImage(){
        return image;
    }
    public void setName(String name){
        this.name=name;
    }
    public void setImage(int  image){
        this.image=image;
    }

}
