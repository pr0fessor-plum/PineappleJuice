package com.example.winstonsmith.pineapplejuice;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Handles playback of all the sound files
    private MediaPlayer mMediaPlayer;

    // Handles audio focus while playing sound file.
    private AudioManager mAudioManager;

    /**
     * This listener gets triggered when the {@link MediaPlayer} has completed
     * playing the audio file.
     */
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };

    // This listener gets triggered whenever audio focus changes
    // i.e., when we gain or lose audio focus due to another app requesting focus
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange ==
                            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        // Pause playback because your Audio Focus was
                        // temporarily stolen, but will be back soon.
                        // i.e. for a phone call
                        mMediaPlayer.pause();
                        mMediaPlayer.seekTo(0);
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        // Resume playback, because you hold the Audio Focus
                        // again!
                        // i.e. the phone call ended or the nav directions
                        // are finished
                        mMediaPlayer.start();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        // Stop playback, because you lost the Audio Focus.
                        // i.e. the user started some other playback app
                        // so stop audio playback and clean up resources
                        releaseMediaPlayer();
                    }
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create and setup the {@link AudioManager} to request audio focus.
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //Create an ArrayList of pineapples.
        final ArrayList<Pineapple> pineapples = new ArrayList<Pineapple>();
        pineapples.add(new Pineapple("My safe word is Pineapple Juice!", R.raw.my_safe_word_is_pineapple_juice));
        pineapples.add(new Pineapple("Bail bonds? Oh shit!", R.raw.bail_bonds_oh_shit));
        pineapples.add(new Pineapple("You fast...", R.raw.you_fast));
        pineapples.add(new Pineapple("That's my flash drive...", R.raw.flash_drive));
        pineapples.add(new Pineapple("Hey, watch your fingers, Booty-Hole man!", R.raw.booty_hole_man));
        pineapples.add(new Pineapple("It's not day, I told you!", R.raw.its_not_day));
        pineapples.add(new Pineapple("You think I'm in Pilates?", R.raw.pilates));
        pineapples.add(new Pineapple("Uuuhh, he strong.", R.raw.he_strong));
        pineapples.add(new Pineapple("In my car, I got snacks...", R.raw.snacks));
        pineapples.add(new Pineapple("I got you.", R.raw.i_got_you));
        pineapples.add(new Pineapple("F@#! you doin'?", R.raw.tazed));
        pineapples.add(new Pineapple("What's your safe word?", R.raw.whats_your_safe_word));
        pineapples.add(new Pineapple("At least your job is fun...", R.raw.job));
        pineapples.add(new Pineapple("Swat Man, what's my safe word?", R.raw.whats_my_safe_word));

        // Create an {@link PineappleAdapter}, whose data source is a list of {@link Pineapple}s. The
        // adapter knows how to create list items for each item in the list.
        PineappleAdapter adapter = new PineappleAdapter(this, pineapples);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // activity_main.xml file.
        GridView listView = (GridView) findViewById(R.id.list);

        // Make the {@link GridView} use the {@link PineappleAdapter} we created above, so that the
        // {@link GridView} will display grid items for each {@link Pineapple} in the list.
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Get the {@link Word} object at the given position.
                Pineapple pineapple = pineapples.get(position);

                // Release the media player if it currently exists because we are about to play another sound.
                releaseMediaPlayer();

                // Request audio focus for a short time to play a short audio file
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

                    //We now have audio focus,...so...

                    // Create and setup the {@link MediaPlayer} for the audio resource associated
                    // with the current word.
                    mMediaPlayer = MediaPlayer.create(MainActivity.this, pineapple.getAudioResourceId());

                    // Start the audio file.
                    mMediaPlayer.start();

                    // Setup a listener on the media player, so that we can stop and release the
                    // media player once the sound has finished playing.
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }

            }
        });
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;

            // Regardless of whether or not we were granted audio focus, abandon it. This also
            // unregisters the AudioFocusChangeListener so we don't get anymore callbacks.
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }


    }
