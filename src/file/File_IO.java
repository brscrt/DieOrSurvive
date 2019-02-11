package file;

import java.io.File;
import java.util.ArrayList;

import game.environment.Map;

public interface File_IO {

	public Map fileRead(File file);

	public boolean fileWrite(File path, ArrayList<String> log);

}
