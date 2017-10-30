package com.example.meetgo;



/**

 * Created by nanto on 10/27/2017.

 */



public class Post {

    private String AuthorName;

    private String postText;

    private int likes;

    private int dislikes;



    public Post(String str, String AuthorName){

        this.postText = str;

        this.AuthorName = AuthorName;

        this.likes = 0;

        this.dislikes = 0;

    }

    // constructor

    public String getPostText(){

        return this.postText;

    }



    public String getAuthorName(){

        return this.AuthorName;

    }



    public void likepost(){

        this.likes++;

    }



    public void dislikepost(){

        this.dislikes++;

    }

}