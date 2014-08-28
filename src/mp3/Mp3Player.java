package mp3;

import utils.Printer;

public class Mp3Player
{
	private Mp3PlayerThread playerThread;
	
	public Mp3Player(String filename)
	{
		playerThread = new Mp3PlayerThread(filename);
	}
	
	public void play()
	{
		playerThread.playing = true;
		playerThread.start();
	}
	public void pause()
	{
		Printer.debugMessage(this.getClass(), "paused");
		playerThread.playing = false;
	}
	public void resume()
	{
		Printer.debugMessage(this.getClass(), "resuming");
		playerThread.playing = true;
	}
	public void stop()
	{
		Printer.debugMessage(this.getClass(), "stopping");
		playerThread.abort = true;
	}
}
