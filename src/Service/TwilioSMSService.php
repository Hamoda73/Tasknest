<?php

namespace App\Service;

use Twilio\Rest\Client;

class TwilioSMSService
{
    private $twilioClient;
    private $twilioPhoneNumber;

    public function __construct(string $accountSid, string $authToken, string $twilioPhoneNumber)
    {
        $this->twilioClient = new Client($accountSid, $authToken);
        $this->twilioPhoneNumber = $twilioPhoneNumber;
    }

    public function sendSMS(string $to, string $message)
    {
        $this->twilioClient->messages->create(
            $to,
            [
                'from' => $this->twilioPhoneNumber,
                'body' => $message
            ]
        );
    }
}
