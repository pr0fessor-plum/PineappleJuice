package com.example.winstonsmith.pineapplejuice;

/**
 * Created by winstonsmith on 12/31/17.
 */

/*{@link Pineapple} represents a phrase that will play when view is clicked on.
 *It contains a title and a sound file.
 */

public class Pineapple {

    //Title for the phrase.
    private String mTitle;

    //Audio resource for the phrase.
    private int mAudioResourceId;

    /**Create a new Word object.
     *
     *@param title is the title of the phrase.
     *
     *@param audioResourceId is the resource ID for the audio file associated with the word
     */
    public Pineapple(String title, int audioResourceId) {
        mTitle = title;
        mAudioResourceId = audioResourceId;
    }

    //    Get the title of the phrase.
    public String getTitle() {
        return mTitle;
    }

    //  Get the Audio Resource for the word.
    public int getAudioResourceId() { return mAudioResourceId;}

}
