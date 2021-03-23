package com.drkstrinc.pokemon.sound;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;

import com.badlogic.gdx.Gdx;

public class MidiPlayer {

	private Sequencer sequencer;

	public MidiPlayer(String fileName) {
		try {
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			sequencer.setSequence(Gdx.files.local(fileName).read());
			sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
		} catch (Exception e) {
			Gdx.app.error("MIDI", e.getMessage(), e);
		}
	}

	public void play() {
		sequencer.start();
	}

	public void pause() {
		sequencer.stop();
	}

	public void stop() {
		sequencer.stop();
		sequencer.setMicrosecondPosition(0);
	}

	public void setLoopStartPoint(long beginTime) {
		sequencer.setLoopStartPoint(beginTime);
	}

	public void setLoopEndPoint(long endTime) {
		sequencer.setLoopEndPoint(endTime);
	}

	public void dispose() {
		sequencer.close();
	}

}
