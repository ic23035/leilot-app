package com.example.leilotparkingstart;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class ParkingDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "parking.db";
    private static final int DATABASE_VERSION = 2; // Increased version to trigger onUpgrade

    public static final String TABLE_NAME = "ParkingSpot";
    public static final String USER_STATS_TABLE = "user_stats";
    public static final String PARKING_SESSIONS_TABLE = "parking_sessions";

    public ParkingDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create ParkingSpot table
        String CREATE_PARKING_SPOT_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "lat REAL, " +
                "lng REAL, " +
                "available INTEGER, " +
                "pricePerHour REAL, " +
                "notes TEXT)";
        db.execSQL(CREATE_PARKING_SPOT_TABLE);

        // Create user_stats table
        String CREATE_USER_STATS_TABLE = "CREATE TABLE " + USER_STATS_TABLE + " (" +
                "userId TEXT PRIMARY KEY, " +
                "totalSessions INTEGER DEFAULT 0, " +
                "totalTime INTEGER DEFAULT 0, " +
                "totalCost REAL DEFAULT 0.0)";
        db.execSQL(CREATE_USER_STATS_TABLE);

        // Create parking_sessions table (optional, for future use)
        String CREATE_PARKING_SESSIONS_TABLE = "CREATE TABLE " + PARKING_SESSIONS_TABLE + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userId TEXT, " +
                "spotId INTEGER, " +
                "startTime INTEGER, " +
                "endTime INTEGER, " +
                "cost REAL, " +
                "FOREIGN KEY(spotId) REFERENCES " + TABLE_NAME + "(id))";
        db.execSQL(CREATE_PARKING_SESSIONS_TABLE);

        // Insert initial parking spots
        insertInitialParkingSpots(db);

        // Insert default user stats
        insertDefaultUserStats(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + USER_STATS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PARKING_SESSIONS_TABLE);
        onCreate(db);
    }

    private void insertDefaultUserStats(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("userId", "1");
        values.put("totalSessions", 0);
        values.put("totalTime", 0);
        values.put("totalCost", 0.0);
        db.insert(USER_STATS_TABLE, null, values);
    }

    private void insertInitialParkingSpots(SQLiteDatabase db) {
        db.execSQL("INSERT INTO " + TABLE_NAME + " (name, lat, lng, available, pricePerHour, notes) VALUES " +
                "('Waikiki Beach Spot 1', 21.27580322981464, -157.8235495090485, 1, 2.5, 'Πάρκινγκ δίπλα στην παραλία Waikiki'), " +
                "('Waikiki Beach Spot 2', 21.27560327753051, -157.8233349323273, 1, 2.0, 'Μικρό πάρκινγκ με περιορισμένες θέσεις'), " +
                "('Kalakaua Corner', 21.275468309585108, -157.82472431659698, 0, 3.0, 'Κοντά στο κέντρο αγορών Kalakaua'), " +
                "('Sunset View Parking', 21.275723248933588, -157.82442390918735, 1, 2.5, 'Εξαιρετική θέα ηλιοβασιλέματος'), " +
                "('Ala Wai Parking 3', 21.276058168582818, -157.8240537643433, 1, 2.0, 'Κοντά στο Ala Wai Canal'), " +
                "('Downtown Spot 1', 21.2764180813859, -157.8236246109009, 1, 2.5, 'Κεντρικό πάρκινγκ με εύκολη πρόσβαση'), " +
                "('Beach Access 5', 21.279102403271573, -157.8231900930405, 0, 3.0, 'Απευθείας πρόσβαση στην παραλία'), " +
                "('Hotel Zone Parking', 21.278917451565565, -157.82336175441745, 1, 3.0, 'Για επισκέπτες ξενοδοχείων'), " +
                "('Shopping Center 2', 21.278717503513715, -157.82357633113864, 1, 2.5, 'Δίπλα σε εμπορικό κέντρο'), " +
                "('Kuhio Avenue Spot', 21.278562543586617, -157.8237158060074, 1, 2.0, 'Σε κεντρικό σημείο του Kuhio'), " +
                "('Park View Parking', 21.278392587349867, -157.82390356063846, 0, 2.5, 'Με θέα towards Kapiolani Park'), " +
                "('Central Waikiki 3', 21.27823262835946, -157.8240537643433, 1, 3.0, 'Ιδανικό για περιπάτους στην πόλη'), " +
                "('Restaurant Row Parking', 21.278112659002524, -157.82423079013827, 1, 2.5, 'Κοντά σε πολλά εστιατόρια'), " +
                "('Nightlife Spot', 21.278002687006044, -157.82440245151523, 0, 3.0, 'Κοντά σε κλαμπ και μπαρ'), " +
                "('Ala Moana Access 1', 21.27867751387073, -157.8226000070572, 1, 2.0, 'Προς Ala Moana Center'), " +
                "('Business District 2', 21.278542548745424, -157.82278776168823, 1, 2.5, 'Για επαγγελματίες της περιοχής'), " +
                "('City Center Parking', 21.278362595052418, -157.82301843166354, 1, 3.0, 'Κοντά σε δημοτικά γραφεία'), " +
                "('Tourist Hub 1', 21.2782076347515, -157.82323300838473, 0, 2.5, 'Κοντά σε τουριστικά γραφεία'), " +
                "('Convention Parking', 21.278037678104752, -157.82342076301578, 1, 3.0, 'Για συνεδριακό κέντρο'), " +
                "('Marina View 2', 21.27787271999515, -157.8236246109009, 1, 2.5, 'Με θέα towards τη μαρίνα'), " +
                "('Residential Zone 3', 21.27768276800347, -157.82383382320407, 1, 2.0, 'Για κατοίκους της περιοχής'), " +
                "('School District Parking', 21.277532805731532, -157.82404839992526, 0, 2.0, 'Κοντά σε σχολεία'), " +
                "('Hospital Access', 21.27812765517748, -157.82291114330292, 1, 2.5, 'Κοντά σε νοσοκομείο'), " +
                "('Public Services Parking', 21.27788771619457, -157.82270193099978, 1, 2.0, 'Για δημόσιες υπηρεσίες'), " +
                "('Library Access', 21.277622783113042, -157.82241225242615, 1, 2.0, 'Δίπλα σε δημόσια βιβλιοθήκη'), " +
                "('Cultural Center', 21.277337854549515, -157.82216012477878, 0, 2.5, 'Για πολιτιστικό κέντρο'), " +
                "('Museum Parking', 21.277027931625824, -157.82226204872134, 1, 2.5, 'Κοντά στο τοπικό μουσείο'), " +
                "('Art District 1', 21.27774775160718, -157.82346367836, 1, 3.0, 'Στην καλλιτεχνική γειτονιά'), " +
                "('Beach Walk Parking', 21.27747781952687, -157.82320082187655, 1, 2.5, 'Στον πεζόδρομο Beach Walk'), " +
                "('Luxury Hotel Parking', 21.277252875748314, -157.82294332981112, 0, 3.0, 'Για επισκέπτες πολυτελών ξενοδοχείων'), " +
                "('Surf School Access', 21.276962947704188, -157.8226697444916, 1, 2.0, 'Κοντά σε σχολές σέρφινγκ'), " +
                "('Sports Complex', 21.276658022765524, -157.82240688800815, 1, 2.5, 'Για αθλητική εγκατάσταση'), " +
                "('Fitness Center Parking', 21.276383089902016, -157.82215476036075, 1, 2.0, 'Για γυμναστήρια της περιοχής'), " +
                "('Yoga Studio Parking', 21.27624312388326, -157.82261073589328, 0, 2.0, 'Κοντά στο κέντρο yoga'), " +
                "('Diamond Head View', 21.28082694177053, -157.8251910209656, 1, 3.0, 'Εξαιρετική θέα προς Diamond Head'), " +
                "('Kapiolani Park West', 21.280457042462288, -157.82563090324405, 1, 2.5, 'Δυτικό τμήμα του πάρκου'), " +
                "('Zoo Access Parking', 21.279987169026835, -157.8260707855225, 0, 2.5, 'Κοντά στον ζωολογικό κήπο'), " +
                "('Aquarium Parking', 21.280247099197883, -157.82446146011355, 1, 2.5, 'Για επισκέπτες ενυδρείου'), " +
                "('Park Center Parking', 21.279837209103853, -157.8248691558838, 1, 2.0, 'Κεντρικό σημείο του πάρκου'), " +
                "('Botanical Garden', 21.279457309948135, -157.82530903816226, 1, 2.0, 'Κοντά σε βοτανικό κήπο'), " +
                "('Picnic Area Parking', 21.279577278208556, -157.82378554344177, 1, 2.0, 'Για περιοχές πικνίκ'), " +
                "('Jogging Path Start', 21.2792973521154, -157.82413959503174, 1, 2.0, 'Στην αρχή του τρεξίματος'), " +
                "('Tennis Courts Parking', 21.279062413733225, -157.8243434429169, 0, 2.0, 'Για γήπεδα τένις'), " +
                "('Soccer Field Parking', 21.27889245807395, -157.82459020614627, 1, 2.0, 'Για γήπεδα ποδοσφαίρου'), " +
                "('Archery Range Parking', 21.278547547455968, -157.82487988471988, 1, 2.0, 'Για βελονοβολία'), " +
                "('Bandstand Parking', 21.27919237969313, -157.82550752162936, 1, 2.5, 'Κοντά στη μουσική σκηνή'), " +
                "('Park Maintenance', 21.27971724105541, -157.82499790191653, 0, 2.0, 'Για υπηρεσίες συντήρησης'), " +
                "('Nature Trail Start', 21.280162122077108, -157.82462775707248, 1, 2.0, 'Στην αρχή του μονοπατιού φύσης'), " +
                "('Duck Pond Parking', 21.28069197861718, -157.82538414001468, 1, 2.0, 'Κοντά στη λίμνη με πάπιες'), " +
                "('Park North Entrance', 21.280257096502975, -157.82578647136688, 1, 2.5, 'Βόρεια είσοδος του πάρκου'), " +
                "('Park South Entrance', 21.279762229085037, -157.82623171806338, 1, 2.5, 'Νότια είσοδος του πάρκου'), " +
                "('Park East Entrance', 21.279457309948135, -157.8265428543091, 1, 2.5, 'Ανατολική είσοδος του πάρκου'), " +
                "('Park West Entrance', 21.279157388869056, -157.82687008380893, 1, 2.5, 'Δυτική είσοδος του πάρκου'), " +
                "('Visitor Center Parking', 21.279042418959968, -157.8270256519318, 0, 2.5, 'Για κέντρο επισκεπτών'), " +
                "('Event Space Parking', 21.278812478872222, -157.82727241516116, 1, 3.0, 'Για χώρους εκδηλώσεων'), " +
                "('Festival Parking', 21.27865252033835, -157.8274065256119, 1, 3.0, 'Κατά τη διάρκεια φεστιβάλ'), " +
                "('Concert Parking', 21.2785075577668, -157.82759964466098, 0, 3.0, 'Για μουσικές εκδηλώσεις'), " +
                "('Food Truck Parking', 21.278332602748847, -157.82778739929202, 1, 2.5, 'Κοντά σε food trucks'), " +
                "('Art Fair Parking', 21.279167386248215, -157.82813072204593, 1, 3.0, 'Για εκθέσεις τέχνης'), " +
                "('Craft Market Parking', 21.27947230598605, -157.82778739929202, 1, 2.5, 'Για λαϊκές αγορές'), " +
                "('Farmer Market Parking', 21.279762229085037, -157.82749235630035, 1, 2.5, 'Για αγροτικές αγορές'), " +
                "('Sunday Market Parking', 21.280092140881965, -157.82714903354648, 0, 2.5, 'Για κυριακάτικες αγορές'), " +
                "('Holiday Market Parking', 21.279487302022414, -157.82804489135745, 1, 3.0, 'Για εποχικές εκδηλώσεις'), " +
                "('Night Market Parking', 21.27968225035618, -157.82826483249667, 1, 3.0, 'Για νυχτερινές αγορές'), " +
                "('Special Event Parking', 21.279917187748463, -157.8285598754883, 0, 3.0, 'Για ειδικές εκδηλώσεις'), " +
                "('VIP Event Parking', 21.279922186412307, -157.82769620418551, 1, 4.0, 'Αποκλειστικό πάρκινγκ'), " +
                "('Media Parking', 21.280142127453313, -157.82791614532474, 0, 3.0, 'Για δημοσιογράφους'), " +
                "('Staff Parking', 21.28038706140743, -157.8282165527344, 1, 2.0, 'Για προσωπικό εκδηλώσεων'), " +
                "('Volunteer Parking', 21.28069197861718, -157.82787859439853, 1, 2.0, 'Για εθελοντές'), " +
                "('Sponsor Parking', 21.28044204652475, -157.82685935497287, 0, 3.5, 'Για χορηγούς εκδηλώσεων'), " +
                "('Exhibitor Parking', 21.28079195133525, -157.82647848129275, 1, 3.0, 'Για εκθέτες'), " +
                "('Performer Parking', 21.281196840148628, -157.82608687877658, 0, 3.0, 'Για καλλιτέχνες'), " +
                "('Vendor Parking', 21.281476762627292, -157.82580792903903, 1, 2.5, 'Για πωλητές'), " +
                "('Security Parking', 21.28157173620438, -157.82702028751376, 1, 2.0, 'Για ασφάλεια'), " +
                "('First Aid Parking', 21.281301811140565, -157.8272241353989, 1, 2.0, 'Για ιατρική βοήθεια'), " +
                "('Information Parking', 21.28112186082283, -157.82741189002994, 1, 2.0, 'Για πληροφορίες'), " +
                "('Accessible Parking', 21.280926914396872, -157.82761573791507, 1, 2.0, 'Για άτομα με ειδικές ανάγκες'), " +
                "('Family Parking', 21.281781677578252, -157.82790005207065, 1, 2.0, 'Για οικογένειες'), " +
                "('Senior Parking', 21.281611725060614, -157.82768547534945, 1, 2.0, 'Για ηλικιωμένους'), " +
                "('Electric Vehicle Parking', 21.281431775121998, -157.8274977207184, 1, 2.5, 'Με φορτιστές ηλεκτρικών'), " +
                "('Bike Share Parking', 21.2808069472371, -157.82675206661227, 1, 2.0, 'Για κοινόχρηστα ποδήλατα'), " +
                "('Scooter Parking', 21.281076873209003, -157.8270471096039, 1, 2.0, 'Για ηλεκτρικά σκούτερ'), " +
                "('Motorcycle Parking', 21.28216656931877, -157.82757818698886, 1, 2.0, 'Για μοτοσικλέτες'), " +
                "('RV Parking', 21.281956628494243, -157.8273153305054, 0, 4.0, 'Για κατασκηνωτικά'), " +
                "('Oversized Vehicle Parking', 21.281781677578252, -157.8271329402924, 1, 3.5, 'Για μεγάλα οχήματα'), " +
                "('Bus Parking', 21.28140178344417, -157.82669842243197, 0, 3.0, 'Για λεωφορεία'), " +
                "('Taxi Stand', 21.281146853935677, -157.82636582851413, 1, 2.5, 'Για ταξί'), " +
                "('Ride Share Parking', 21.281076873209003, -157.8287744522095, 1, 2.5, 'Για Uber/Lyft'), " +
                "('Valet Parking', 21.2808069472371, -157.8290319442749, 0, 5.0, 'Υπηρεσία παρκαρίσματος'), " +
                "('Hotel Valet Parking', 21.280457042462288, -157.82931089401248, 0, 5.0, 'Αποκλειστικό για ξενοδοχεία'), " +
                "('Resort Parking', 21.280227104585638, -157.82966494560245, 1, 4.0, 'Για επισκέπτες θέρετρων'), " +
                "('Spa Parking', 21.279897193091386, -157.83007264137268, 1, 3.0, 'Για σπα'), " +
                "('Golf Course Parking', 21.279557283505284, -157.83058762550357, 1, 3.0, 'Για γήπεδο γκολφ'), " +
                "('Country Club Parking', 21.2790674124261, -157.83112406730655, 0, 3.5, 'Για μέλη'), " +
                "('Private Club Parking', 21.278657519045172, -157.83146739006042, 0, 4.0, 'Αποκλειστικό για μέλη'), " +
                "('Marina Parking', 21.27887746197693, -157.83245444297793, 1, 3.0, 'Για μαρίνα'), " +
                "('Yacht Club Parking', 21.27849756034282, -157.8318536281586, 0, 3.5, 'Για λέσχη σκαφών'), " +
                "('Boat Ramp Parking', 21.277817733917516, -157.83103823661807, 1, 3.0, 'Για μπάρκα'), " +
                "('Fishing Pier Parking', 21.278347598901387, -157.83058762550357, 1, 2.5, 'Για αλιευτικό μόλο'), " +
                "('Surf Spot Parking', 21.283236263152613, -157.82797515392306, 1, 2.5, 'Για σέρφερς'), " +
                "('Dive Shop Parking', 21.28292135409363, -157.8282541036606, 1, 2.5, 'Για καταδύσεις'), " +
                "('Kayak Rental Parking', 21.282636435792632, -157.8285920619965, 1, 2.5, 'Για ενοικίαση καγιάκ'), " +
                "('Paddleboard Parking', 21.282341519776992, -157.8288817405701, 1, 2.5, 'Για paddleboarding'), " +
                "('Snorkeling Spot Parking', 21.28374611448529, -157.82853305339813, 1, 2.5, 'Για snorkeling'), " +
                "('Beach Equipment Parking', 21.28345120069462, -157.8288280963898, 1, 2.0, 'Για ενοικίαση εξοπλισμού'), " +
                "('Sunset Cruise Parking', 21.28316628341999, -157.82912850379947, 1, 3.0, 'Για κρουαζιέρες'), " +
                "('Whale Watching Parking', 21.282846375646873, -157.82939136028293, 1, 3.0, 'Για παρατήρηση φαλαινών'), " +
                "('Helicopter Tour Parking', 21.285035730555254, -157.83013701438904, 1, 3.0, 'Για αεροπορικές ξεναγήσεις'), " +
                "('Airport Shuttle Parking', 21.284735820850408, -157.8305017948151, 1, 2.5, 'Για λεωφορεία προς αεροδρόμιο'), " +
                "('Rental Car Parking', 21.284325943264726, -157.83099532127383, 1, 2.5, 'Για ενοικίαση αυτοκινήτων'), " +
                "('Tour Bus Parking', 21.285555572594884, -157.83069491386416, 0, 3.0, 'Για τουριστικά λεωφορεία'), " +
                "('Limousine Parking', 21.285255663950117, -157.83101677894595, 0, 4.0, 'Για λιμουζίνες'), " +
                "('Party Bus Parking', 21.284995742629945, -157.83126354217532, 0, 3.5, 'Για πάρτι λεωφορεία'), " +
                "('Wedding Parking', 21.284725823849723, -157.8314781188965, 1, 3.0, 'Για γάμους'), " +
                "('Photoshoot Parking', 21.286055418977035, -157.83117771148682, 1, 3.0, 'Για φωτογραφίσεις'), " +
                "('Film Location Parking', 21.285835486778186, -157.8313493728638, 0, 3.0, 'Για γυρίσματα'), " +
                "('Production Parking', 21.285645545069052, -157.83164978027347, 0, 3.0, 'Για παραγωγές'), " +
                "('Crew Parking', 21.285415615303428, -157.8319072723389, 1, 2.5, 'Για συνεργεία'), " +
                "('Talent Parking', 21.285155694265956, -157.8321433067322, 0, 3.5, 'Για καστ'), " +
                "('Ala Moana Regional Park', 21.288424667716235, -157.8352761268616, 1, 2.5, 'Κύριο πάρκινγκ του πάρκου'), " +
                "('Magic Island Parking', 21.28830470667224, -157.83614516258243, 1, 2.5, 'Για Magic Island'), " +
                "('Ala Moana Mall Parking', 21.288114768152397, -157.83691763877871, 0, 3.0, 'Για εμπορικό κέντρο'), " +
                "('Kakaako Waterfront', 21.287994806855572, -157.83764719963077, 1, 2.5, 'Για παράλια Kakaako'), " +
                "('Ward Village Parking', 21.287834858307562, -157.83854842185977, 1, 3.0, 'Για Ward Village'), " +
                "('Fisherman Wharf Parking', 21.287614928769973, -157.83934235572818, 1, 3.0, 'Για αλιευτικό λιμάνι'), " +
                "('Kewalo Basin Parking', 21.28744498299291, -157.83699274063113, 1, 2.5, 'Για λιμάνι ψαριών'), " +
                "('Harbor View Parking', 21.286995125576187, -157.83630609512332, 1, 2.5, 'Με θέα στο λιμάνι'), " +
                "('Pier 38 Parking', 21.286575257412284, -157.8354370594025, 1, 2.5, 'Για προβλήτα 38'), " +
                "('Commercial Harbor', 21.28707510032862, -157.8370034694672, 0, 3.0, 'Για εμπορικό λιμάνι'), " +
                "('Cargo Terminal Parking', 21.286635238651954, -157.83738970756534, 0, 3.0, 'Για φορτωτικά'), " +
                "('Cruise Ship Parking', 21.28636532288079, -157.837690114975, 1, 4.0, 'Για κρουαζιερόπλοια'), " +
                "('Ferry Terminal Parking', 21.286075412796993, -157.83809781074527, 1, 3.0, 'Για φέρι'), " +
                "('Coast Guard Parking', 21.285845483703458, -157.838408946991, 0, 2.5, 'Για ακτοφυλακή'), " +
                "('Marine Base Parking', 21.28559556036797, -157.83791542053225, 0, 2.5, 'Για ναυτική βάση'), " +
                "('Harbor Patrol Parking', 21.285265660914803, -157.83737897872928, 0, 2.5, 'Για περιπολία λιμένος'), " +
                "('Customs Parking', 21.28559556036797, -157.83648848533633, 0, 2.5, 'Για τελωνείο'), " +
                "('Immigration Parking', 21.28622536635996, -157.8366279602051, 0, 2.5, 'Για υπηρεσίες μετανάστευσης'), " +
                "('Port Authority Parking', 21.28644529797575, -157.8370463848114, 0, 2.5, 'Για λιμενική αρχή'), " +
                "('Shipping Office Parking', 21.286795188504783, -157.83584475517276, 1, 2.5, 'Για ναυτιλιακά γραφεία'), " +
                "('Maritime Museum Parking', 21.286455294859557, -157.83508300781253, 1, 2.5, 'Για ναυτικό μουσείο');");
    }

    public UserStats getCachedUserStats(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        UserStats userStats = null;

        try {
            Cursor cursor = db.rawQuery("SELECT totalSessions, totalTime, totalCost FROM " + USER_STATS_TABLE + " WHERE userId = ?", new String[]{userId});
            if (cursor.moveToFirst()) {
                int totalSessions = cursor.getInt(0);
                long totalTime = cursor.getLong(1);
                double totalCost = cursor.getDouble(2);
                userStats = new UserStats(totalSessions, totalTime, totalCost);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            // Return default stats if query fails
            userStats = new UserStats(0, 0, 0.0);
        } finally {
            db.close();
        }

        // If no user found, create default user and return default stats
        if (userStats == null) {
            insertDefaultUserIfNotExists(userId);
            userStats = new UserStats(0, 0, 0.0);
        }

        return userStats;
    }

    private void insertDefaultUserIfNotExists(String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("userId", userId);
            values.put("totalSessions", 0);
            values.put("totalTime", 0);
            values.put("totalCost", 0.0);
            db.insertWithOnConflict(USER_STATS_TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    // Alternative calculation methods
    public int getTotalSessions(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;
        try {
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + PARKING_SESSIONS_TABLE + " WHERE userId = ?", new String[]{userId});
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return count;
    }

    public double getTotalSpent(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        double total = 0.0;
        try {
            Cursor cursor = db.rawQuery("SELECT SUM(cost) FROM " + PARKING_SESSIONS_TABLE + " WHERE userId = ?", new String[]{userId});
            if (cursor.moveToFirst()) {
                total = cursor.getDouble(0);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return total;
    }

    public long getTotalTime(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        long total = 0;
        try {
            Cursor cursor = db.rawQuery("SELECT SUM(endTime - startTime) FROM " + PARKING_SESSIONS_TABLE + " WHERE userId = ?", new String[]{userId});
            if (cursor.moveToFirst()) {
                total = cursor.getLong(0);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return total;
    }

    public void updateUserStats(int userId, int sessions, long time, double cost) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("totalSessions", sessions);
            values.put("totalTime", time);
            values.put("totalCost", cost);

            int rowsAffected = db.update(USER_STATS_TABLE, values, "userId = ?", new String[]{String.valueOf(userId)});

            // If no rows were affected, insert new record
            if (rowsAffected == 0) {
                values.put("userId", String.valueOf(userId));
                db.insert(USER_STATS_TABLE, null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public UserStats getUserStats(int userId) {
        return getCachedUserStats(String.valueOf(userId));
    }

    public void insertParkingSpot(ParkingSpot spot) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("name", spot.getName());
            values.put("lat", spot.getLat());
            values.put("lng", spot.getLng());
            values.put("available", spot.isAvailable() ? 1 : 0);
            values.put("pricePerHour", spot.getPricePerHour());
            values.put("notes", spot.getNotes());

            db.insert(TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public List<ParkingSpot> getAllSpots() {
        List<ParkingSpot> spotList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    double lat = cursor.getDouble(2);
                    double lng = cursor.getDouble(3);
                    boolean available = cursor.getInt(4) == 1;
                    double price = cursor.getDouble(5);
                    String notes = cursor.getString(6);

                    ParkingSpot spot = new ParkingSpot(id, name, lat, lng, available, price, notes);
                    spotList.add(spot);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return spotList;
    }

    public List<ParkingSpot> getAllAvailableSpots() {
        List<ParkingSpot> spotList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE available = 1", null);
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    double lat = cursor.getDouble(2);
                    double lng = cursor.getDouble(3);
                    boolean available = cursor.getInt(4) == 1;
                    double price = cursor.getDouble(5);
                    String notes = cursor.getString(6);

                    ParkingSpot spot = new ParkingSpot(id, name, lat, lng, available, price, notes);
                    spotList.add(spot);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return spotList;
    }

    public void reserveSpot(int spotId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("available", 0); // Mark as not available
            db.update(TABLE_NAME, values, "id = ?", new String[]{String.valueOf(spotId)});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    // Release
    public void releaseSpot(int spotId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("available", 1); // Mark as available again
            db.update(TABLE_NAME, values, "id = ?", new String[]{String.valueOf(spotId)});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public ParkingSpot getSpotById(int spotId) {
        SQLiteDatabase db = this.getReadableDatabase();
        ParkingSpot spot = null;

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE id = ?", new String[]{String.valueOf(spotId)});
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                double lat = cursor.getDouble(2);
                double lng = cursor.getDouble(3);
                boolean available = cursor.getInt(4) == 1;
                double price = cursor.getDouble(5);
                String notes = cursor.getString(6);

                spot = new ParkingSpot(id, name, lat, lng, available, price, notes);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return spot;
    }
}