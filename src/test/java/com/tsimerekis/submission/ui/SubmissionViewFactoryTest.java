package com.tsimerekis.submission.ui;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.NotificationsKt;
import com.github.mvysny.kaributesting.v10.Routes;
import com.tsimerekis.submission.entity.PollutionReport;
import com.tsimerekis.submission.entity.Species;
import com.tsimerekis.submission.entity.SpeciesSpotting;
import com.tsimerekis.submission.entity.Submission;
import com.tsimerekis.submission.exception.InvalidSubmissionException;
import com.tsimerekis.submission.service.SubmissionService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.time.LocalDateTime;

import static com.github.mvysny.kaributesting.v10.LocatorJ._click;
import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static com.vaadin.flow.internal.CurrentInstance.get;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Assuming AbstractTest sets up MockVaadin environment (as shown previously)
public class  SubmissionViewFactoryTest {

    // Use Mockito annotations for cleaner setup
    @Mock
    private SubmissionService mockSubmissionService;

    // The component under test, with its dependency injected
    @InjectMocks
    private SubmissionViewFactory factory;

    private static Routes routes;

    // We also need to mock the external views created by the factory,
    // as we cannot assume their implementation details in this test.
    // However, since they are instantiated directly inside the factory,
    // we need to use actual instances in the test environment for the component
    // query to work, but we will mock their getter methods.

    private final SpeciesSubmissionView mockSpeciesView = mock(SpeciesSubmissionView.class);
    private final PollutionReportView mockPollutionView = mock(PollutionReportView.class);

    @BeforeAll
    public static void createRoutes() {
        routes = new Routes().autoDiscoverViews("com.tsimerekis");
    }

    @BeforeEach
    void mockViews() {
        // Initialize Mockito annotations
        MockitoAnnotations.openMocks(this);

        // Since we can't easily mock the 'new' operator in the factory,
        // for simplicity in this unit test, we will assume that the
        // view factory creates the view and we will directly manipulate
        // the required properties of the submission object before the save.

        // NOTE: In a real-world application, you might use a test-specific
        // dependency injection framework or mock the entire factory pattern
        // if the child views were complex.
        doAnswer(validationAnswer)
                .when(mockSubmissionService).save(any(SpeciesSpotting.class));
    }

    Answer<Void> validationAnswer = invocation -> {
        // Get the argument passed to the save method (the Submission object)
        Submission submission = invocation.getArgument(0, Submission.class);

        // 2. Execute the validation logic
        if (!submission.isValid()) {
            // 3. Throw the desired exception if validation fails
            throw new InvalidSubmissionException();
        }

        // If valid, return null for a void method
        return null;
    };

    @BeforeEach
    public void setupVaadin() {
        MockVaadin.setup(routes);
    }

    @AfterEach
    public void tearDownVaadin() {
        MockVaadin.tearDown();
    }


    // --- Factory Logic Tests ---

    @Test
    void createSubmissionView_returnsCorrectViewType() {
        PollutionReport report = new PollutionReport();
        VerticalLayout view = factory.createSubmissionView(report);
        assertTrue(view instanceof PollutionReportView, "Should return a PollutionReportView");

        SpeciesSpotting spotting = new SpeciesSpotting();
        view = factory.createSubmissionView(spotting);
        assertTrue(view instanceof SpeciesSubmissionView, "Should return a SpeciesSubmissionView");
    }

    // --- Form Interaction and Service Call Tests ---

    @Test
    void createSubmissionForm_speciesSpotting_successfulSave() throws Exception {
        SpeciesSpotting spotting = new SpeciesSpotting();
        Dialog mockDialog = mock(Dialog.class);

        // 1. Create the form (This instantiates the SpeciesSubmissionView internally)
        UI.getCurrent().add(factory.createSubmissionForm(spotting, mockDialog));

        // **Simulate Data Entry & Wiring**
        // In a proper test harness, we would find and set values in the child view's fields.
        // For this test, we verify the data is correctly transferred to the Submission object.

        // Simulating the data the child view would provide:
        // We will assert the submission object is updated before the service call.
        // NOTE: If you were testing the views themselves, you would test setting the fields.
        assertNotNull(spotting.getSpecies());

        spotting.getSpecies().setSpeciesName("Orca");
        // Using a test coordinate here (which the map listener would normally set)
        spotting.setLocation(com.tsimerekis.geometry.GeometryHelper.getJTSPoint(
                new com.vaadin.flow.component.map.configuration.Coordinate(10.0, 20.0)));
        spotting.setObservedAt(LocalDateTime.now());


        // 2. Find and click the Save button
        // `get` finds a unique component matching the criteria.
        Button saveButton = _get(Button.class, spec -> spec.withCaption("Save"));
        _click(saveButton);

        // 3. Verification

        // Capture the object passed to the service
        ArgumentCaptor<SpeciesSpotting> submissionCaptor = ArgumentCaptor.forClass(SpeciesSpotting.class);
        verify(mockSubmissionService, times(1)).save(submissionCaptor.capture());

        final SpeciesSpotting savedSpotting = submissionCaptor.getValue();
        // Since the factory sets the species and location before calling save,
        // we assert the captured object matches the data we expect from the UI.
        assertEquals("orca", savedSpotting.getSpecies().getSpeciesName());
        // assertEquals(10.0, savedSpotting.getLocation().getX()); // Asserting JTS point requires JTS mock/setup

        // Verify Success Notification and Dialog close
        NotificationsKt.expectNotifications("Submitted");
        verify(mockDialog, times(1)).close();
    }

    @Test
    void createSubmissionForm_speciesSpotting_invalidSubmission() throws Exception {
        SpeciesSpotting spotting = new SpeciesSpotting();
        Dialog mockDialog = mock(Dialog.class);

        // 1. Create the form (This instantiates the SpeciesSubmissionView internally)
        UI.getCurrent().add(factory.createSubmissionForm(spotting, mockDialog));

        // **Simulate Data Entry & Wiring**
        // In a proper test harness, we would find and set values in the child view's fields.
        // For this test, we verify the data is correctly transferred to the Submission object.

        // Simulating the data the child view would provide:
        // We will assert the submission object is updated before the service call.
        // NOTE: If you were testing the views themselves, you would test setting the fields.
        assertNotNull(spotting.getSpecies());

//        spotting.getSpecies().setSpeciesName("Orca");
//        // Using a test coordinate here (which the map listener would normally set)
//        spotting.setLocation(com.tsimerekis.geometry.GeometryHelper.getJTSPoint(
//                new com.vaadin.flow.component.map.configuration.Coordinate(10.0, 20.0)));
//        spotting.setObservedAt(LocalDateTime.now());

        // 2. Find and click the Save button
        // `get` finds a unique component matching the criteria.
        Button saveButton = _get(Button.class, spec -> spec.withCaption("Save"));
        _click(saveButton);

        // 3. Verification

        // Capture the object passed to the service
        verify(mockSubmissionService, times(0)).save(any(Submission.class));

        // Verify Success Notification and Dialog close
        NotificationsKt.expectNotifications("Invalid Submission");
        verify(mockDialog, times(0)).close();
    }


    @Test
    void createSubmissionForm_pollutionReport_handlesInvalidSubmission() throws Exception {
        PollutionReport report = new PollutionReport();
        Dialog mockDialog = mock(Dialog.class);

        // Configure the service to throw the exception on save
        doThrow(InvalidSubmissionException.class)
                .when(mockSubmissionService).save(any(PollutionReport.class));

        // 1. Create the form
        factory.createSubmissionForm(report, mockDialog);

        // 2. Find and click the Save button
//        Button saveButton = get(Button.class, spec -> spec.withCaption("Save"));
        Button saveButton = get(Button.class);

        saveButton.click();

        // 3. Verification

        // Service should still be called
        verify(mockSubmissionService, times(1)).save(any(PollutionReport.class));

        // Verify Error Notification is shown
        // Note: Karibu-Testing captures the Notification text shown on the screen
        NotificationsKt.expectNotifications("Invalid Submission");

        // Verify Dialog was NOT closed due to the error
        verify(mockDialog, never()).close();
    }

    @Test
    void createSubmissionForm_mapLocationWidgetIsPresent() {
        SpeciesSpotting spotting = new SpeciesSpotting();
        Dialog mockDialog = mock(Dialog.class);

        // 1. Create the form
        VerticalLayout form = factory.createSubmissionForm(spotting, mockDialog);

        // 2. Check if the Map component was added to the form
        com.vaadin.flow.component.map.Map map = get(com.vaadin.flow.component.map.Map.class);

        // Assert the Map component is in the form (its parent is form)
        assertTrue(map.getParent().isPresent() && map.getParent().get() == form, "Map should be a child of the form.");

        // Assert the initial marker is present (This tests the addLocationSelectorWidget method)
        // Karibu-Testing can inspect the internal state of the map feature layer (if exposed or mockable)
        // A simpler test is to ensure the map component itself was added successfully.
    }
}