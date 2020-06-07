import javax.print.DocFlavor;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static String fpmLink = "https://www.fpm.iastate.edu/maps/buildings/floorplan.asp?id=";
    public static String downloadLink = "https://www.fpm.iastate.edu/maps/buildings/";
    public static List<String>  buildingNames = Arrays.asList(
            "Administrative Services Building","Advanced Machinery Systems Laboratory","Advanced Teaching and Research Building","Agronomy Greenhouse","Agronomy Hall","Alumni Center","Ames Intermodal Facility","Ames Lab Construction Storage","Ames Lab Maintenance Shops ","Ames Lab Mechanical Maintenance","Ames Lab Paint and Air Conditioning","Ames Lab Warehouse","Applied Science 1","Applied Science 2","Applied Science 3","Applied Science 4","Armory","Atanasoff Hall","Barton Residence Hall","Beardshear Hall","Bergstrom Football Complex","Bergstrom Indoor Training Facility","Bessey Hall","Beyer Hall","Biomass Reprocessing Facility","Biorenewables Research Laboratory","Birch Residence Hall","Black Engineering","Buchanan Residence Hall","Campanile","Carver Hall","Catt Hall","Central Receiving Facility","College of Design","Communications Building","Coover Hall","Crop Genome Informatics Laboratory","Curtiss Hall","Cyclone Sports Complex Concessions and Restrooms","Cyclone Sports Complex Locker Rooms","Cyclone Sports Complex Maintenance Building","Cyclone Sports Complex Soccer Pressbox","Cyclone Sports Complex Softball Pressbox","Cyclone Sports Complex Track Storage","Dairy Milking Center (DF-10)","Durham Center","East Hall","East Parking Deck","Eaton Residence Hall","Economic Development Core Facility","Elings Hall","Enrollment Services Center","Environmental Health and Safety Services Building","Extension 4-H Building","Extension Information Technology","Family Resource Center","Farm House","Firemanship Training","Fisher Theater","Food Sciences Building","Forestry Greenhouse","Forker Building","Frederiksen Court 11","Frederiksen Court 12","Frederiksen Court 13","Frederiksen Court 21","Frederiksen Court 22","Frederiksen Court 23","Frederiksen Court 24","Frederiksen Court 31","Frederiksen Court 32","Frederiksen Court 33","Frederiksen Court 34","Frederiksen Court 35","Frederiksen Court 36","Frederiksen Court 41","Frederiksen Court 42","Frederiksen Court 43","Frederiksen Court 51","Frederiksen Court 52","Frederiksen Court 53","Frederiksen Court 61","Frederiksen Court 62","Frederiksen Court 63","Frederiksen Court 71","Frederiksen Court 72","Frederiksen Court 73","Frederiksen Court 74","Frederiksen Court 81","Frederiksen Court 82","Frederiksen Court 83","Frederiksen Court Community Center","Freeman Residence Hall","Friley Residence Hall","General Services Building","Genetics Chick Isolation","Genetics Laboratory","Geoffroy Hall","George A. Jackson Black Cultural Center","Gerdin Business Building","Gilman Hall","Hach Hall","Hamilton Hall","Hansen Agriculture Student Learning Center","Heady Hall","Helser Residence Hall","Hilton Coliseum","Hixson-Lied Student Success Center","Hoover Hall","Horse Barn","Horticulture Hall","Howe Hall","Hub","Human Nutritional Sciences Building","Ice Arena","Insectary","Iowa Farm Bureau Pavilion","Jack Trice Stadium","Jacobson Athletic Building","Jischke Honors Building","Kildee Hall","King Pavilion","Kingland Campus Properties, LLC.","Knapp-Storms Dining Complex","Knoll","Laboratory of Mechanics","Lagomarcino Hall","Landscape Architecture","Larch Residence Hall","LeBaron Hall","Library Storage Facility","Lied Recreation Athletic Facility","Linden Residence Hall","Lloyd Veterinary Medical Center","Lynn Fuhrer Lodge","Lyon Residence Hall","MacKay Hall","Maple Residence Hall","Maple-Willow-Larch Commons","Marston Hall","Martin Residence Hall","Meats Laboratory","Memorial Union","Metals Development Building","Molecular Biology Building","Morrill Hall","Music Hall","National Laboratory for Agriculture and the Environment","National Swine Research and Information Center","North Chilled Water Plant","Oak-Elm Residence Hall","Office and Laboratory Building","Olsen Building","Palmer Building","Parks Library","Pearson Hall","Physics Hall","Plant Pathology Greenhouse","Power Plant","Printing and Publications Building","Reiman Gardens Conservatory","Reiman Gardens Hunziker House","Reiman Gardens Mahlstede Center","Reiman Maintenance Building","Research Park 1","Research Park 11","Research Park 2","Research Park 3","Research Park 4","Research Park 5","Research Park 6","Research Park 7","Research Park 9","Roberts Residence Hall","Ross Hall","Roy J. Carver Co-Lab","Ruminant Nutrition Laboratory","Scheman Building","Schilletter-University Village Community Center","Science Hall","Science Hall II","Seed Science Building","Sensitive Instrument Facility","Sloss House","Snedecor Hall","South Campus Storage Facility","Spedding Hall","State Avenue Office Building","State Avenue Warehouse","State Gymnasium","Stephens Auditorium","Student Services","Sukup Basketball Complex","Sukup Hall","SW Paintball Storage Shed","Sweeney Hall","Technical and Administrative Services Facility","Thielen Student Health Center","Town Engineering Building","Transit Hub","Transportation Services","Troxel Hall","Union Drive Community Center","University Child Care Center at Veterinary Medicine","University Surplus and Storage Facility","USDA Greenhouse","Veenker Clubhouse","Veenker Maintenance Shed","Veenker Performance Center","Vet Med Performance Evaluation Facility","Vet Med Power Plant","Veterinary Field Services","Veterinary Medicine","Visitor Information Booth","Wallace Residence Hall","Wallace-Wilson Commons","Waste Chemical Handling","Welch Residence Hall","Wilhelm Hall","Willow Residence Hall","Wilson Residence Hall","Workiva","Zaffarano Physics Addition"
    );
    public static List<String> buildingIds = Arrays.asList("1","193","283","4","5","191","247","227","229","228","230","226","150","151","152","175","10","11","12","13","248","167","14","15","232","221","16","17","18","19","20","21","264","31","24","25","6","27","270","271","269","272","268","273","282","32","33","165","149","278","257","7","176","161","35","170","37","38","39","41","42","43","194","195","196","197","198","199","255","200","201","202","203","250","251","204","205","206","207","208","209","210","211","212","213","214","215","216","252","253","254","153","44","45","46","173","47","281","261","164","48","222","49","256","51","52","53","178","163","54","55","56","57","58","156","61","62","65","66","147","67","218","274","177","69","71","72","74","75","76","77","78","79","192","262","82","83","85","168","86","171","87","88","89","90","91","92","93","94","172","34","97","98","100","102","103","104","105","106","107","154","179","84","109","64","280","185","186","187","220","188","217","277","110","111","159","112","113","183","115","116","117","266","118","119","155","121","259","260","122","123","126","219","258","279","127","128","125","129","148","130","249","158","131","184","134","180","181","182","246","224","225","137","138","139","169","190","141","143","144","145","276","162");
    public static void main(String[] args) {
        for(int i = 0; i< buildingIds.size(); i++){
            System.out.println(buildingNames.get(i));
            getLinks(fpmLink + buildingIds.get(i), buildingNames.get(i));
        }

    }

    public static void getLinks(String url,  String name){
        String dir = ((System.getenv("USERPROFILE"))+("\\My Documents\\"))  + File.separator + "isu-FloorPlans";
        String dirName = dir + File.separator + name + File.separator ;
        try{
            File f = new File(dir);
            f.mkdir();
        }catch(Exception e){

        }
        try{
            File f = new File(dirName);
            f.mkdir();
        }catch(Exception e){

        }
        try {
           String html = SocketConnection.getURLSource(url);

            ArrayList<String> links = new ArrayList<String>();
            String regex = "href=\"(.*?)\"";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(html);
            int index = 0;
            while (matcher.find(index)) {
                String wholething = matcher.group(); // includes "<a href" and ">"
                String link = matcher.group(1); // just the link
                links.add(link);
                // do something with wholething or link.
                index = matcher.end();
            }
            for(int i = 0; i<links.size(); i++){

                if (links.get(i).contains("docs")){
                    System.out.println(links.get(i));
                    String newLink = links.get(i).replace(" ", "%20");
                    saveFiles(downloadLink + newLink, dirName);
                }
            }
        }catch(Exception e){
            System.out.println("Oh no");
        }
    }
    public static void saveFiles(String url2, String directory){
        try {;
             System.out.println("opening connection");
             URL url = new URL(url2);
             InputStream in = url.openStream();
             FileOutputStream fos = new FileOutputStream(new File(directory + url2.substring( url2.lastIndexOf('/')+1, url2.length() )));

             System.out.println("reading from resource and writing to file...");
             int length = -1;
             byte[] buffer = new byte[1024];// buffer for portion of data from connection
             while ((length = in.read(buffer)) > -1) {
                fos.write(buffer, 0, length);
            }
            fos.close();
            in.close();
            System.out.println("File downloaded");
        } catch (IOException e) {
            System.out.println("And I oop" + e.getMessage());
        }
    }
}
