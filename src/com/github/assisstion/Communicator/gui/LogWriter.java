package com.github.assisstion.Communicator.gui;

import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogWriter extends Writer {
	private Logger log;
	private Level level;
	private String buff;
	private boolean closed;

	public LogWriter(Logger log, Level level) {
		this.log = log;
		this.level = level;
		lock = new Object();
		buff = "";
		closed = false;
	}

	@Override
	public void write(char[] c, int off, int len) throws IOException{
		if(closed){
			throw new IllegalStateException();
		}
		if (off < 0 || off > c.length || len < 0 ||
				off + len > c.length || off + len < 0) {
			throw new IndexOutOfBoundsException();
		}
		synchronized(lock){
			char[] buf = new char[len];
			System.arraycopy(c, off, buf, 0, len);
			String string = String.copyValueOf(buf);
			buff += string;
			if(string.contains("\n")){
				flush();
			}
		}
	}

	@Override
	public void flush() throws IOException{
		if(closed){
			throw new IllegalStateException();
		}
		synchronized(lock){
			int i = buff.lastIndexOf("\n");
			if(i >= 0){
				buff = buff.substring(0, i) + buff.substring(i + 1);
			}
			log.log(level, buff);
			buff = "";
		}
	}

	@Override
	public void close() throws IOException{
		closed = true;
	}

}
