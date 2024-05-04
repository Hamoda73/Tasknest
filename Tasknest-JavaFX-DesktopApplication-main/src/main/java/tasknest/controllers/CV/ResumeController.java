package tasknest.controllers.CV;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import tasknest.models.CV;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import tasknest.models.Skill;
import tasknest.services.CvService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.scene.control.ProgressBar;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.FileDataSource;
import javafx.scene.control.Alert;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
public class ResumeController {
    private CV customizedCV;
    private CvService cvservice;
    @FXML
    private Label locat;
    @FXML
    private VBox language;
    @FXML
    private ImageView imagecv;

    @FXML
    private Label contact;
    @FXML
    private Label bio;
    @FXML
    private Label certification;
    @FXML
    private Label experience;
    @FXML
    private Label name;
    @FXML
    private Label email;
    @FXML
    private Label number;
    @FXML
    private Label value;
    @FXML
    private Label skillN;
    @FXML
    private ProgressBar prog;
    @FXML
    private HBox root1;
    @FXML
    private VBox skillContainer;

int i=0;
    public void setCustomizedCV(CV cv) {
        customizedCV = cv;
        cvservice=new CvService();
        System.out.println("\n customizedCV=" + customizedCV);
        if(i==0){
        fillCVWithData(cv); i++;}
        if(i==1) {
            generatePDF();i++;
        }
        if(i==2){generateAndSendPDF(cvservice,customizedCV);}
    }





    public void setRoot(HBox root11) {
        this.root1 = root11;
    }

    private void fillCVWithData(CV cv) {
        System.out.println("hi");
        String username = cvservice.getUserInfoForCV(cv.getId());
        String imageuser = cvservice.getUserImageForCV(cv.getId());
        String useremail = cvservice.getUserEMAILForCV(cv.getId());
        int usernumber = cvservice.getUserphonenumberForCV(cv.getId());
        // Assuming cv.getDescription() returns the experience description
        if (experience != null && cv != null) {
            String description = cv.getDescription();
            String contactText = cv.getContact();
            if (description != null && !description.isEmpty()) {
                experience.setText(description);
                experience.setMaxWidth(400);
                experience.setWrapText(true);


                contact.setText(contactText);
                contact.setMaxWidth(210);
                contact.setWrapText(true);
            }
        }
        // Assuming this is your VBox in the FXML file

        String[] languages = cv.getLanguage();
        for (String languageStr : languages) {
            // Remove the square brackets and double quotes from the language string
            String cleanLanguage = languageStr.replace("[", "").replace("]", "").replace("\"", "");
            // Create a Label for each language with a point before it
            Label languageLabel = new Label("• " + cleanLanguage.trim());
            languageLabel.setFont(new Font("Calibri", 18)); // Adjust the font size as needed
            languageLabel.setTextFill(Color.web("#23265f")); // Set the color code as needed

            VBox.setMargin(languageLabel, new Insets(0, 0, 0, 10)); // Adjust the left inset as needed

            // Add the label to the VBox
            language.getChildren().add(languageLabel);
        }


// Inside the fillCVWithData method
        if (imageuser != null && !imageuser.isEmpty()) {
            // Load the image from the file path or URL
            URL imageUrl = getClass().getResource("/images/" + imageuser);
            javafx.scene.image.Image image = new javafx.scene.image.Image(imageUrl.toExternalForm());
            ImageView imageView = new ImageView(image);
            // Set the image to your existing ImageView
            imagecv.setImage(image); // Replace yourImageView with the name of your ImageView in your FXML

            Circle clip = new Circle(75); // Set the radius according to your image size
            clip.setCenterX(75); // Set the center X coordinate of the circle
            clip.setCenterY(75); // Set the center Y coordinate of the circle

            // Apply the circular clip to the ImageView
            imagecv.setClip(clip);

            // Set the fit width and height of the ImageView to match the circle diameter
            imagecv.setFitWidth(150);
            imagecv.setFitHeight(150);

            // Optionally, set the preserve ratio property to true to maintain the aspect ratio of the image
            imagecv.setPreserveRatio(true);

        }
        String locationn = cv.getLocation();
        locat.setText(locationn);
        String bioo = cv.getBio();
        bio.setText(bioo);
        String certif = cv.getCertification();
        certification.setText(certif);


        if (username != null) {
            name.setText(username);
            // Set other user information as needed
        }

        if (useremail != null) {
            email.setText(useremail);
            // Set other user information as needed
        }
        if (usernumber != 0) {
            number.setText(Integer.toString(usernumber));
            // Set other user information as needed
        }


        //skillllllllllllllllllllllllllllllllssssssssssssssssssssssssssss

        List<Skill> skills = cvservice.getSkillsForCV(cv.getId());

// Check if there are any skills associated with the CV
        skillContainer.getChildren().clear(); // Clear existing content
        skillContainer.setSpacing(10); // Adjust the spacing value as needed
// Iterate over the skills and create VBox for each skill
        for (Skill skill : skills) {
            // Create a new VBox for each skill
            VBox vbox = new VBox();

            // Create HBox to hold skill name and value
            HBox hbox = new HBox();

            // Create Label for skill name
            Label skillNameLabel = new Label(skill.getName());
            HBox.setMargin(skillNameLabel, new Insets(0, 6, 0, 6)); // Insets(top, right, bottom, left)
            skillNameLabel.setFont(new Font("Calibri", 14)); // Adjust the font size as needed
            skillNameLabel.setTextFill(Color.web("#23265f")); // Set the color code as needed
            // Create Label for skill value
            Label skillValueLabel = new Label(String.valueOf(skill.getSkill_value()) + "%");
            HBox.setMargin(skillValueLabel, new Insets(0, 0, 0, 6)); // Insets(top, right, bottom, left)
            skillNameLabel.setFont(new Font("Calibri", 14)); // Adjust the font size as needed
            skillNameLabel.setTextFill(Color.web("#23265f")); // Set the color code as needed
            ProgressBar skillProgressBar = new ProgressBar(skill.getSkill_value() / 100.0);
            HBox.setMargin(skillProgressBar, new Insets(3, 0, 0, 0)); // Insets(top, right, bottom, left)

            skillProgressBar.setPrefHeight(10); // Adjust the height value as needed

            // Add skill name and value to the HBox
            hbox.getChildren().addAll(skillNameLabel, skillProgressBar,skillValueLabel);

            // Create ProgressBar for skill progress


            // Add components to the VBox
            vbox.getChildren().addAll(hbox);

            // Add VBox to the skillContainer VBox
            skillContainer.getChildren().add(vbox);
        }

    }
























        public void generatePDF() {
        // Create a temporary file to store the image
        File tempImageFile = new File("temp_image.png");

        try {
            // Capture the content of the JavaFX scene as a WritableImage
            javafx.scene.image.WritableImage writableImage = new javafx.scene.image.WritableImage((int) 793, (int) 1122);
            root1.snapshot(null, writableImage);

            // Write the WritableImage to a temporary file
            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", tempImageFile);

            // Read the image file to BufferedImage
            BufferedImage originalImage = ImageIO.read(tempImageFile);

            // Calculate the height of the resized image while maintaining aspect ratio
            int newHeight = (int) (originalImage.getHeight() * (793.0 / originalImage.getWidth()));

            // Resize the image to match the dimensions of the PDF page
            BufferedImage resizedImage = new BufferedImage(793, newHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(originalImage, 0, 0, 793, newHeight, null);
            g.dispose();

            // Convert the resized BufferedImage to iText Image
            Image itextImage = Image.getInstance(resizedImage, null);

            // Create a PDF document
            Document document = new Document();

            // Create PDF writer
            FileOutputStream fos = new FileOutputStream("cusresume12.pdf");
            PdfWriter writer = PdfWriter.getInstance(document, fos);

            // Open document
            document.open();

            // Set image position to the top-left corner of the page
            itextImage.setAlignment(Element.ALIGN_TOP | Element.ALIGN_LEFT);
            itextImage.setAbsolutePosition(0, document.getPageSize().getHeight() - itextImage.getScaledHeight());


            // Add image to the document
            document.add(itextImage);

            // Close document
            document.close();

            // Close the writer
            writer.close();

            // Delete the temporary image file
            tempImageFile.delete();

            // Show success alert
            showAlert(Alert.AlertType.INFORMATION, "Success", "PDF generated successfully.");
            System.out.println("byeeeeeeeeeeeeeeee222222222222222222");

            System.out.println("byeeeeeeeeeeeeeeee222222222222222222");
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }

    }

   /* @FXML
    public void downloadPDF(ActionEvent actionEvent) {
        if (this.root1 != null) {
            generatePDF();
            System.out.println(root1);
        } else {
            System.out.println("Root container is null. Make sure it is properly initialized.");
        }
    }*/

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }








    public void generateAndSendPDF(CvService cvs, CV cv) {
        // Generate the PDF
        String useremail = cvs.getUserEMAILForCV(cv.getId());

        // Email configuration
        String to = useremail;
        String from = "majus.erij1@gmail.com"; // Replace with your Gmail address
        String password = "dplc kmtq lwbr ggnn"; // Replace with your Gmail password
        String host = "smtp.gmail.com"; // Gmail SMTP server
        int port = 587; // Port for Gmail SMTP

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", Integer.toString(port)); // Set the SMTP port
        properties.setProperty("mail.smtp.auth", "true"); // Enable authentication
        properties.setProperty("mail.smtp.starttls.enable", "true"); // Enable STARTTLS

        // Get the default Session object with authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            // Create a MimeMessage
            MimeMessage message = new MimeMessage(session);

            // Set From: header field
            message.setFrom(new InternetAddress(from));

            // Set To: header field
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("TASKNEST : Your Customized CV");

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Fill the message body
            messageBodyPart.setText("TASKNEST : Please find attached your CV.");

            // Create a multipart message
            Multipart multipart = new MimeMultipart();

            // Add text message part
            multipart.addBodyPart(messageBodyPart);

            // Add attachment part
            messageBodyPart = new MimeBodyPart();
            String filename = "cusresume12.pdf"; // Replace with your PDF file name
            FileDataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);

            // Set the complete message parts
            message.setContent(multipart);

            // Send message
            Transport.send(message);
            System.out.println("Email sent successfully...");

            // Show alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Email Sent");
            alert.setHeaderText(null);
            alert.setContentText("Please check your Gmail to find your CV.");
            alert.showAndWait();
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }



}
