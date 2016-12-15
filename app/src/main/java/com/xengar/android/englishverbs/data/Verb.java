package com.xengar.android.englishverbs.data;

import static com.xengar.android.englishverbs.data.VerbContract.VerbEntry;

/**
 * Data that is contained for a verb item
 */
public class Verb {

    private String infinitive;
    private String simplePast;
    private String pastParticiple;
    private String definition;
    private String samples = "";
    private int common = VerbEntry.OTHER;
    private int regular = VerbEntry.REGULAR;
    private int color = 0;
    private int score = 0;
    private String data = "";
    private String translationES = "";
    private String translationFR = "";

    /*** Constructor */
    public Verb(String infinitive, String simplePast, String pastParticiple,
                String definition, String samples, int common, int regular, int color,
                int score, String data, String translationES, String translationFR){
        this.infinitive = infinitive;
        this.simplePast = simplePast;
        this.pastParticiple = pastParticiple;
        this.definition = definition;
        this.samples = samples;
        this.common = common;
        this.regular = regular;
        this.color = color;
        this.score = score;
        this.data = data;
        this.translationES = translationES;
        this.translationFR = translationFR;
    }

    /* Getters and Setters */
    public String getInfinitive() { return infinitive; }
    public void setInfinitive(String infinitive) { this.infinitive = infinitive; }

    public String getSimplePast() { return simplePast; }
    public void setSimplePast(String simplePast) { this.simplePast = simplePast; }

    public String getPastParticiple() { return pastParticiple; }
    public void setPastParticiple(String pastParticiple) { this.pastParticiple = pastParticiple; }

    public String getDefinition() { return definition; }
    public void setDefinition(String definition) { this.definition = definition; }

    public String getSamples() { return samples; }
    public void setSamples(String samples) { this.samples = samples; }

    public int getCommon() { return common; }
    public void setCommon(int common) { this.common = common; }

    public int getRegular() { return regular; }
    public void setRegular(int regular) { this.regular = regular; }

    public int getColor() { return color; }
    public void setColor(int color) { this.color = color; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getTranslationES() { return translationES; }
    public void setTranslationES(String translationES) { this.translationES = translationES; }

    public String getTranslationFR() { return translationFR; }
    public void setTranslationFR(String translationFR) { this.translationFR = translationFR; }
}
