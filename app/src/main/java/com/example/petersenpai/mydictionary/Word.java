package com.example.petersenpai.mydictionary;

public class Word {
    private String spell;
    private String translation;
    private Boolean show;

    Word(String word, String translation){
        this.spell = word;
        this.translation = translation;
        this.show = true;
    }

    Word(){
        this.spell = "";
        this.translation = "";
        this.show = true;
    }

    public String getSpell(){
        return spell;
    }

    public void setSpell(String spell){
        this.spell = spell;
    }

    public String getTranslation(){
        return translation;
    }

    public void setTranslation(String trans){
        this.translation = trans;
    }

    public Boolean getShow() {
        return show;
    }

    public void setShow() {
        if (this.show == true){
            this.show = false;
        }else {
            this.show = true;
        }
    }
}