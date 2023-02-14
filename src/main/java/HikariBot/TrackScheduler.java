package HikariBot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class TrackScheduler extends AudioEventAdapter {
    
    public final AudioPlayer player;
    public final BlockingQueue<AudioTrack> queue;
    
    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingDeque<>();
    }
    
    @Override
    public void onPlayerPause(AudioPlayer player) {
      player.setPaused(true);
    }

    @Override
    public void onPlayerResume(AudioPlayer player) {
      player.setPaused(false);
    }

    public void queue(AudioTrack track) {
      if (!player.startTrack(track, true)) {
        queue.offer(track);
      }
    }

    public void nextTrack() {
        player.startTrack(queue.poll(), false);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
      if (endReason.mayStartNext) {
        nextTrack();
      }

      // endReason == FINISHED: A track finished or died by an exception (mayStartNext = true).
      // endReason == LOAD_FAILED: Loading of a track failed (mayStartNext = true).
      // endReason == STOPPED: The player was stopped.
      // endReason == REPLACED: Another track started playing while this had not finished
      // endReason == CLEANUP: Player hasn't been queried for a while, if you want you can put a
      //                       clone of this back to your queue
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        System.out.println("Error !! "+exception+" on "+track);
        player.startTrack(track, true);
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
      System.out.println("Error !! "+String.valueOf(thresholdMs)+" on "+track);
    }
}
