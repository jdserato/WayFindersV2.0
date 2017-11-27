import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by next on 10/18/2017.
 */
public class DatabaseHelper {

    private static final String DATABASE_URL = "https://wayfinders-d7e57.firebaseio.com/";

    private static boolean isInitialized = false;

    public static void initFirebase() throws IOException {
        try {
            // [START initialize]
            if(!isInitialized){
                FileInputStream serviceAccount = new FileInputStream("src/wayfinders-d7e57-firebase-adminsdk-fsckn-5fdf91643f.json");
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
                        .setDatabaseUrl(DATABASE_URL)
                        .build();
                FirebaseApp.initializeApp(options);
                isInitialized = true;
            }

            // [END initialize]
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: invalid service account credentials. See README.");
            System.out.println(e.getMessage());

            System.exit(1);
        }
    }
}