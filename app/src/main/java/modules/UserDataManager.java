package modules;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by nbissiri on 11/17/2016.
 * UserDataManager tracks the user's favorites, likes, and dislikes,
 * loads this data on startup, and exposes setting/removing and
 * getting of this data
 */

public class UserDataManager {
    private static UserDataManager singletonInstance;

    // data sets
    private Set<String> favoriteRoutesByID;
    private Set<Integer> favoriteNeighborhoodsByID;
    private Set<Integer> upVotedAlertsByID;
    private Set<Integer> downVotedAlertsByID;
    private Set<Integer> upVotedCommentsByID;
    private Set<Integer> downVotedCommentsByID;
    private File fileDir;

    // TODO: make this private and figure out a better way
    public String userID;

    /**
     * Gets the manager
     * @return The singleton instance of this class or null
     * if instantiateManager() has not yet been called
     */
    public static UserDataManager getManager() {
        return singletonInstance;
    }

    /**
     *
     * @return set of favorite routes by String ID
     */
    public Set<String> getFavoriteRoutesByID() {
        return favoriteRoutesByID;
    }

    /**
     * Add and remove neighborhoodID's from this set as necessary
     * @return set of favorite neighborhoods by Integer ID
     */
    public Set<Integer> getFavoriteNeighborhoodsByID() {
        return favoriteNeighborhoodsByID;
    }

    /**
     * Add and remove from this set as necessary
     * @return set of upvoted alerts by Integer ID
     */
    public Set<Integer> getUpVotedAlertsByID() {
        return upVotedAlertsByID;
    }

    /**
     * Add and remove from this set as necessary
     * @return set of downvoted alerts by Integer ID
     */
    public Set<Integer> getDownVotedAlertsByID() {
        return downVotedAlertsByID;
    }

    /**
     * Add and remove from this set as necessary
     * @return set of upvoted comments by Integer ID
     */
    public Set<Integer> getUpVotedCommentsByID() {
        return upVotedCommentsByID;
    }

    /**
     * Add and remove from this set as necessary
     * @return set of downvoted comments by Integer ID
     */
    public Set<Integer> getDownVotedCommentsByID() {
        return downVotedCommentsByID;
    }

    /**
     * Saves user data to files in the directory of the activity used to instantiate this
     */
    public void saveUserData(Activity activity) {
        FileOutputStream outputStream;
        ObjectOutputStream objOutputStream;
        String[] fileNames =
                {"FavoriteRoutes",
                 "FavoriteNeighborhoods",
                 "UpVotedAlerts",
                 "DownVotedAlerts",
                 "UpVotedComments",
                 "DownVotedComments",
                 "UserID"};
        Object[] fileObjects =
                {favoriteRoutesByID,
                 favoriteNeighborhoodsByID,
                 upVotedAlertsByID,
                 downVotedAlertsByID,
                 upVotedCommentsByID,
                 downVotedCommentsByID,
                 userID};
        for (int i = 0; i < fileNames.length; i ++) {
            try {
                outputStream = activity.openFileOutput(fileNames[i], Context.MODE_PRIVATE);
                objOutputStream = new ObjectOutputStream(outputStream);
                objOutputStream.writeObject(fileObjects[i]);
                objOutputStream.close();
                outputStream.close();
            } catch (IOException e) {
                Log.d("Error", "Error saving file:\n" +
                        "\tFilename: " + fileNames[i] +
                        "\n\tObject: " + fileObjects.toString() + "\n");
                e.printStackTrace();
            }
        }
    }
    /**
     * Instantiates the manager. The user must be an AppCompatActivity, and must
     * pass itself to this method. Nothing happens after the first call.
     * Loads data files from the given activity's file directory.
     * @param activity The Activity which created this
     */
    public static void instantiateManager(Activity activity) {
        if (singletonInstance == null) {
            singletonInstance = new UserDataManager(activity);
        }
    }

    /**
     * Destroys the current instance of the manager, for use in testing
     * Do not use this to create multiple instances of the manager
     */
    public static void destroyManager() {
        singletonInstance = null;
    }

    private void loadSavedData() {
        File[] files = fileDir.listFiles();
        favoriteRoutesByID = null;
        favoriteNeighborhoodsByID = null;

        for (File f : files) {
            try {
                if (f.isFile()) {
                    FileInputStream fileIn = new FileInputStream(f);
                    ObjectInputStream objIn = new ObjectInputStream(fileIn);
                    String name = f.getName();
                    if (name.equals("FavoriteRoutes")) {
                        favoriteRoutesByID = (Set<String>) objIn.readObject();
                    } else if (name.equals("FavoriteNeighborhoods")) {
                        favoriteNeighborhoodsByID = (Set<Integer>) objIn.readObject();
                    } else if (name.equals("UpVotedAlerts")) {
                        upVotedAlertsByID = (Set<Integer>) objIn.readObject();
                    } else if (name.equals("DownVotedAlerts")) {
                        downVotedAlertsByID = (Set<Integer>) objIn.readObject();
                    } else if (name.equals("UpVotedComments")) {
                        upVotedCommentsByID = (Set<Integer>) objIn.readObject();
                    } else if (name.equals("DownVotedComments")) {
                        downVotedCommentsByID = (Set<Integer>) objIn.readObject();
                    } else if (name.equals("UserID")) {
                        userID = (String) objIn.readObject();
                    }
                }
            } catch (IOException e) {
                System.err.println("Error loading user data: IO failed, filname: " + f.getName());
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println("Error loading user data: class not found, filename: " + f.getName());
                e.printStackTrace();
            }
        }

        if (favoriteNeighborhoodsByID == null) {
            favoriteNeighborhoodsByID = new HashSet<>();
        }
        if (favoriteRoutesByID == null) {
            favoriteRoutesByID = new HashSet<>();
        }
        if (upVotedAlertsByID == null) {
            upVotedAlertsByID = new HashSet<>();
        }
        if (downVotedAlertsByID == null) {
            downVotedCommentsByID = new HashSet<>();
        }
        if (upVotedCommentsByID == null) {
            upVotedCommentsByID = new HashSet<>();
        }
        if (downVotedCommentsByID == null) {
            downVotedCommentsByID = new HashSet<>();
        }

    }
    private UserDataManager(Activity activity) {
        this.fileDir = activity.getFilesDir();
        loadSavedData();
    }
}
