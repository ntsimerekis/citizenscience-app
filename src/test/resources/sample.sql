INSERT INTO species (species_id, species_name) VALUES (1, 'homo sapien');
INSERT INTO submission (
    submission_id,
    type,
    observed_at,
    location,
    pm25,
    pm10,
    duration_hours,
    temperature_celsius,
    altitude_meters,
    sensor_type,
    note,
    status
)
VALUES (
 1,
    'POLLUTION',
           '2025-10-13T14:30:00Z',
           ST_SetSRID(ST_MakePoint(-122.4194, 37.7749), 4326)::geometry,
           12.5,
           35.0,
           30.0,                   -- duration in minutes
           21.5,                   -- temperature in °C
           50.0,                   -- altitude in meters
           'AirVisual Pro',
           'Clear day, light wind',
           'SUBMITTED'
       );

INSERT INTO submission (
    submission_id,
    observed_at,
    location,
    note,
    created_at,
    status,
    type,
    species_species_id,
    temperature_celsius,
    altitude_meters
)
VALUES (2,
        '2025-10-13T18:30:00Z',
        ST_SetSRID(ST_MakePoint(-122.4194, 37.7749), 4326)::geometry,
        'Spotted near the park around dusk.',
        now(),
        'SUBMITTED',
        'SPECIES',
        1,
        16.5,
        120.0);
INSERT INTO submission (
    submission_id,
    observed_at,
    location,
    note,
    created_at,
    status,
    type,
    species_species_id,
    temperature_celsius,
    altitude_meters
)
VALUES (
           3,
           '2025-10-14T09:15:00Z',
           ST_SetSRID(ST_MakePoint(-74.0060, 40.7128), 4326)::geometry,
           'Observed in an urban park on the east coast.',
           now(),
           'SUBMITTED',
           'SPECIES',
           1,
           18.2,
           15.0
       );
