/*
 * Copyright (C) 2017 Angel Newton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xengar.android.englishverbs.data;

import static com.xengar.android.englishverbs.data.VerbContract.VerbEntry;

/**
 * Data that is contained for a verb item.
 */
public class Verb {

    private long id;
    private String infinitive;
    private String simplePast;
    private String pastParticiple;
    private String definition;
    private String phoneticInfinitive;
    private String phoneticSimplePast;
    private String phoneticPastParticiple;
    private String sample1 = "";
    private String sample2 = "";
    private String sample3 = "";
    private int common = VerbEntry.OTHER;
    private int regular = VerbEntry.REGULAR;
    private int color = 0;
    private int score = 0;
    private int source = 0;
    private String notes = "";
    private String translationES = "";
    private String translationFR = "";

    /*** Constructor */
    public Verb(long id, String infinitive, String simplePast, String pastParticiple,
                String definition, String sample1, String sample2, String sample3,
                String phoneticInfinitive, String phoneticSimplePast,
                String phoneticPastParticiple,
                int common, int regular, int color, int score, int source, String notes,
                String translationES, String translationFR){
        this.id = id;
        this.infinitive = infinitive;
        this.simplePast = simplePast;
        this.pastParticiple = pastParticiple;
        this.definition = definition;
        this.phoneticInfinitive = phoneticInfinitive;
        this.phoneticSimplePast = phoneticSimplePast;
        this.phoneticPastParticiple = phoneticPastParticiple;
        this.sample1 = sample1;
        this.sample2 = sample2;
        this.sample3 = sample3;
        this.common = common;
        this.regular = regular;
        this.color = color;
        this.score = score;
        this.source = source;
        this.notes = notes;
        this.translationES = translationES;
        this.translationFR = translationFR;
    }

    /* Getters and Setters */
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getInfinitive() { return infinitive; }
    public void setInfinitive(String infinitive) { this.infinitive = infinitive; }
    public String getSimplePast() { return simplePast; }
    public void setSimplePast(String simplePast) { this.simplePast = simplePast; }
    public String getPastParticiple() { return pastParticiple; }
    public void setPastParticiple(String pastParticiple) { this.pastParticiple = pastParticiple; }

    public String getPhoneticInfinitive() { return phoneticInfinitive; }
    public void setPhoneticInfinitive(String phoneticInfinitive) { this.phoneticInfinitive = phoneticInfinitive; }
    public String getPhoneticSimplePast() { return phoneticSimplePast; }
    public void setPhoneticSimplePast(String phoneticSimplePast) { this.phoneticSimplePast = phoneticSimplePast; }
    public String getPhoneticPastParticiple() { return phoneticPastParticiple; }
    public void setPhoneticPastParticiple(String phoneticPastParticiple) { this.phoneticPastParticiple = phoneticPastParticiple; }

    public String getSample1() { return sample1; }
    public void setSample1(String sample1) { this.sample1 = sample1; }
    public String getSample2() { return sample2; }
    public void setSample2(String sample1) { this.sample2 = sample2; }
    public String getSample3() { return sample3; }
    public void setSample3(String sample3) { this.sample3 = sample3; }

    public String getDefinition() { return definition; }
    public void setDefinition(String definition) { this.definition = definition; }

    public int getCommon() { return common; }
    public void setCommon(int common) { this.common = common; }

    public int getRegular() { return regular; }
    public void setRegular(int regular) { this.regular = regular; }

    public int getColor() { return color; }
    public void setColor(int color) { this.color = color; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getSource() { return source; }
    public void setSource(int source) { this.source = source; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getTranslationES() { return translationES; }
    public void setTranslationES(String translationES) { this.translationES = translationES; }

    public String getTranslationFR() { return translationFR; }
    public void setTranslationFR(String translationFR) { this.translationFR = translationFR; }
}
