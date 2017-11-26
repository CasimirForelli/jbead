/** jbead - http://www.jbead.ch
    Copyright (C) 2001-2017  Damian Brunold

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package ch.jbead.audio;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JProgressBar;

import ch.jbead.BeadList;
import ch.jbead.BeadRun;
import ch.jbead.Localization;
import ch.jbead.Settings;

/**
 *
 */
public class TalkingManager {

    public static final String DEFAULT_PATH_TO_TTS = "%HOME%\\tts\\tts.exe";

    public static final String DEFAULT_TTS_PARAMS = "-f 3 -v 0 -o <out> <text>";

    public static final String DEFAULT_FILE_TYPE = "wav";

    public static final String DEFAULT_PATH_TO_SPEACH_FILES = "%TMP%\\jbead_audio_cache";;

    private String pathToTts;
    private String pathToSpeachFiles;
    private String ttsParams;
    private String fileType;
    // private String ttsParams = "-f 3 -v 0 -t -o <out> <text>";
    // private String fileType = "mp3";

    private AudioFilePlayer player;

    public TalkingManager() {
        player = new AudioFilePlayer();

        final Settings settings = new Settings();
        settings.setCategory("audio");

        pathToTts = resolvePath(settings.loadString("pathToTts", DEFAULT_PATH_TO_TTS));
        ttsParams = settings.loadString("ttsParams", DEFAULT_TTS_PARAMS);
        fileType = settings.loadString("fileType", DEFAULT_FILE_TYPE);
        pathToSpeachFiles = resolvePath(settings.loadString("pathToSpeachFiles", DEFAULT_PATH_TO_SPEACH_FILES));
    }

    /**
     * @param loadString
     * @return
     */
    private String resolvePath(String inPath) {
        String finalPath = "";
        if (inPath.contains("%TMP%")) {
            String tmpDir = System.getenv("TMP");
            finalPath = inPath.replace("%TMP%", tmpDir);
        }
        if (inPath.contains("%HOME%")) {
            String homeDir = "";
            if (System.getProperty("os.name").contains("indows")) {
                homeDir = System.getenv("HOMEDRIVE") + System.getenv("HOMEPATH");
            } else {
                homeDir = System.getenv("HOME");
            }
            finalPath = inPath.replace("%HOME%", homeDir);
        }
        if (!finalPath.endsWith(System.getProperty("file.separator"))) {
            finalPath = finalPath + System.getProperty("file.separator");
        }
        return finalPath;
    }

    /**
     *
     * @return
     */
    private boolean prepareCache() {
        boolean ok = false;
        File cacheDir = new File(pathToSpeachFiles);
        if (!cacheDir.exists()) {
            ok = cacheDir.mkdirs();
        }
        return ok;
    }

    /**
     *
     */
    public void clearCache() {

        File cacheDir = new File(pathToSpeachFiles);
        if (!cacheDir.exists()) {
            prepareCache();
            return;
        }
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith("." + fileType);
            }
        };
        File[] audioFiles = cacheDir.listFiles(filter);
        for (int i = 0; i < audioFiles.length; i++) {
            File file = audioFiles[i];
            file.delete();
        }

    }

    /**
     *
     * @param text
     * @return
     */
    public boolean createAudioFile(String text) {
        return createAudioFile(text, text);
    }

    public boolean createAudioFile(String fileName, String text) {
        boolean ok = false;

        String outFilename = fileName + "_";
        String outFileFullpath = pathToSpeachFiles + System.getProperty("file.separator") + outFilename + "0." + fileType;

        File outFile = new File(outFileFullpath);
        if (outFile.exists()) {
            return true;
        }

        String params = ttsParams.replace("<out>", outFilename);
        params = params.replace("<text>", text);

        String pa[] = params.split(" ");
        List<String> pList = new ArrayList<>();
        pList.add(pathToTts);
        pList.addAll(Arrays.asList(pa));

        File workingDir = new File(pathToSpeachFiles);
        prepareCache();

        ProcessBuilder pb = new ProcessBuilder(pList);
        pb.directory(workingDir);
        try {
            Process p = pb.start();

            // TODO improve by using a timeout
            int exitCode = p.waitFor();
            if (exitCode == 0) {
                ok = true;
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ok;
    }

    /**
     *
     * @param list
     * @param bar
     * @param colorNameMap
     */
    public void createBeadListAudios(BeadList list, JProgressBar bar, Map<Byte, String> colorNameMap, Localization localization) {

        Set<Integer> numberSet = new TreeSet<Integer>();
        Set<Byte> colorSet = new TreeSet<Byte>();

        if (bar != null) {
            bar.setMinimum(0);
            bar.setMaximum(list.size());
            bar.setValue(0);
        }
        // Create Message audio
        if (localization != null) {
            String msg = localization.getString("talkingdialog.pattern.end");
            createAudioFile("talkingdialog.pattern.end", msg);
        }

        // Create Color and number files
        int idx = 0;
        for (BeadRun beadRun : list) {
            if (bar != null) bar.setValue(++idx);

            // create number file
            int number = beadRun.getCount();
            if (!numberSet.contains(number)) {
                String numberAsText = Integer.toString(number);
                if (createAudioFile(numberAsText)) {
                    numberSet.add(number);
                } else {
                    throw new RuntimeException("Error creating number audio file");
                }
            }

            // create color file
            byte color = beadRun.getColor();
            if (!colorSet.contains(color)) {
                String colorAsText = colorNameMap.get(color);
                if (createAudioFile(colorAsText)) {
                    colorSet.add(color);
                } else {
                    throw new RuntimeException("Error creating color audio file");
                }
            }

        }

    }

    /**
     *
     * @param beadRun
     */
    public void speak(BeadRun beadRun, Map<Byte, String> colorNameMap) {

        String numberFile = pathToSpeachFiles + beadRun.getCount() + "_0." + fileType;
        String colorName = colorNameMap.get(beadRun.getColor());
        String colorFile = pathToSpeachFiles + colorName + "_0." + fileType;

        System.out.println("Speaking: " + colorFile);
        player.play(colorFile);
        System.out.println("Speaking: " + numberFile);
        player.play(numberFile);

        return;
    }

    /**
     * @param textKey
     */
    public void speakText(String textKey) {
        String textSpeachFile = pathToSpeachFiles + textKey + "_0." + fileType;

        System.out.println("Speaking: " + textSpeachFile);
        player.play(textSpeachFile);

    }

}
