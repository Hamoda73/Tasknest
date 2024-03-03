<?php
// The URL to your image
$imageUrl = 'https://127.0.0.1:8000/images/userpdp.png';

// Create a stream context that disables SSL verification
$context = stream_context_create([
    "ssl" => [
        "verify_peer" => false,
        "verify_peer_name" => false,
    ]
]);

// Use file_get_contents() with the custom stream context to get the image data
$imageData = file_get_contents($imageUrl, false, $context);

if ($imageData !== false) {
    // Encode the image data to Base64
    $base64Image = base64_encode($imageData);
    // Assuming the image is a JPEG; adjust as necessary for your use case
    echo 'data:image/png;base64,' . $base64Image;
} else {
    echo "The URL could not be accessed or is not an image.";
}
