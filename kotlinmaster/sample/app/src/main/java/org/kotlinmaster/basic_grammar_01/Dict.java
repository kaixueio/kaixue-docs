package org.kotlinmaster.basic_grammar_01;


public class Dict {

    private String word;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;

        KotlinDict kotlinDict = new KotlinDict();
        kotlinDict.getWord();
        kotlinDict.setWord("hello");
    }

}
