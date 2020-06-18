#!/bin/sh

gpg --quiet --batch --yes --decrypt --passphrase="$GOOGLE_SERVICES_PASS_PHRASE" \
--output app/google-services.json app/google-services.secure.gpg
