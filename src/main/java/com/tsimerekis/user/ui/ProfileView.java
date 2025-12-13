package com.tsimerekis.user.ui;

import com.tsimerekis.bucket.BucketService;
import com.tsimerekis.user.UserEntity;
import com.tsimerekis.user.UserService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

@Route("profile")
@PageTitle("My Profile")
@Menu(order = 100, icon = "vaadin:user", title = "Profile")
@PermitAll
public class ProfileView extends VerticalLayout {

    private final BucketService bucketService;

    private final UserService userService;

    @Autowired
    public ProfileView(AuthenticationContext authContext,
                       UserService userService,
                       BucketService bucketService) {
        this.bucketService = bucketService;
        this.userService = userService;

        setSizeFull();
        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        getStyle().set("padding", "var(--lumo-space-xl)");

        userService
                .getUserEntityFromUserDetails(
                        authContext.getAuthenticatedUser(UserDetails.class).get()
                )

                .ifPresent(user -> add(buildContent(user)));
    }

    private Div buildContent(UserEntity user) {
        Div card = new Div();
        card.getStyle()
                .set("max-width", "600px")
                .set("width", "100%")
                .set("border-radius", "var(--lumo-border-radius-l)")
                .set("box-shadow", "var(--lumo-box-shadow-s)")
                .set("padding", "var(--lumo-space-l)")
                .set("background-color", "var(--lumo-base-color)");

        // Header: avatar + name + username
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setSpacing(true);

        Avatar avatar = new Avatar(user.getFullName());
        avatar.setWidth("64px");
        avatar.setHeight("64px");

        VerticalLayout nameBlock = new VerticalLayout();
        nameBlock.setPadding(false);
        nameBlock.setSpacing(false);

        H2 fullName = new H2(user.getFullName());
        fullName.getStyle().set("margin", "0");

        Text username = new Text("@" + user.getUsername());

        nameBlock.add(fullName, username);
        header.add(avatar, nameBlock);

        // Profile picture preview + download link + upload
        VerticalLayout imageSection = new VerticalLayout();
        imageSection.setPadding(false);
        imageSection.setSpacing(true);
        imageSection.setWidthFull();

        if (user.getProfilePicturePath() != null && !user.getProfilePicturePath().isBlank()) {
            // Signed URL for viewing/downloading
            final String signedURLString = bucketService
                    .getSignedDownloadURL(user.getProfilePicturePath())
                    .toString();

            avatar.setImage(signedURLString);

            Image profileImage = new Image(signedURLString, "Profile picture");
            profileImage.setMaxWidth("250px");
            profileImage.getStyle()
                    .set("border-radius", "var(--lumo-border-radius-m)")
                    .set("box-shadow", "var(--lumo-box-shadow-xs)");

            Anchor downloadAnchor = new Anchor(signedURLString, "Download photo");
            downloadAnchor.getElement()
                    .setAttribute("download", "profile-picture-" + user.getId() + ".jpg");
            downloadAnchor.getStyle()
                    .set("margin-top", "var(--lumo-space-s)")
                    .set("font-size", "var(--lumo-font-size-s)");

            imageSection.add(profileImage, downloadAnchor);
        } else {
            imageSection.add(new Paragraph("No profile picture uploaded."));
        }

        // --- Upload new profile picture section (server-side upload to GCS) ---
        Paragraph uploadLabel = new Paragraph("Update profile photo");
        uploadLabel.getStyle()
                .set("font-weight", "600")
                .set("margin-top", "var(--lumo-space-m)")
                .set("margin-bottom", "var(--lumo-space-xs)");

//        MemoryBuffer buffer = new MemoryBuffer();
//        Upload upload = new Upload(buffer);
//        upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/webp");
//        upload.setMaxFiles(1);
//        upload.setDropLabel(new Paragraph("Drop new profile photo here"));
//        upload.setUploadButton(new Button("Select new photo"));
//
//        upload.addSucceededListener(event -> {
//            try (InputStream is = buffer.getInputStream()) {
//                // Upload to GCS via BucketService (this uses the signing key on the backend)
//                String objectPath = bucketService.uploadProfilePicture(
//                        user.getId(),
//                        event.getFileName(),
//                        is,
//                        event.getMIMEType()
//                );
//
//                // Update user entity and persist
//                user.setProfilePicturePath(objectPath);
//                userService.updateUser(user);
//
//                Notification.show("Profile picture updated", 3000, Position.BOTTOM_CENTER);
//
//                // Reload page so the signed download URL + image refresh
//                getUI().ifPresent(ui -> ui.getPage().reload());
//
//            } catch (IOException e) {
//                Notification.show("Upload failed: " + e.getMessage(),
//                        5000, Position.MIDDLE);
//            }
//        });
//
//        upload.addFileRejectedListener(event ->
//                Notification.show("File rejected: " + event.getErrorMessage(),
//                        5000, Position.MIDDLE)
//        );

//        imageSection.add(uploadLabel, upload);

        // Biography
        VerticalLayout bioSection = new VerticalLayout();
        bioSection.setPadding(false);
        bioSection.setSpacing(false);

        Paragraph bioLabel = new Paragraph("Biography");
        bioLabel.getStyle()
                .set("font-weight", "600")
                .set("margin-bottom", "var(--lumo-space-xs)");

        String bioText = (user.getBiography() == null || user.getBiography().isBlank())
                ? "You haven't written a bio yet."
                : user.getBiography();
        Paragraph bioContent = new Paragraph(bioText);
        bioContent.getStyle()
                .set("white-space", "pre-wrap");

        bioSection.add(bioLabel, bioContent);

        card.add(header, imageSection, bioSection);
        return card;
    }
}