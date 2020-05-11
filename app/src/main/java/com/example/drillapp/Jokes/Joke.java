package com.example.drillapp.Jokes;



import androidx.appcompat.app.AppCompatActivity;

public class Joke  {
    //tänne voi lisätä id ja kategoria jne.
    String jokeContent;


    public Joke(){
        this.jokeContent="";
    }
    public void setJokeContent(String value){
        this.jokeContent=value;
    }
    public String getJokeContent(){
        return  this.jokeContent;

    }
}



