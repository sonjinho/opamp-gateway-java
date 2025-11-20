package io.opentelemetry.opamp.client.domain.agent;

public record ConnectionSettingsRequestDomain(OpampConnectionSettingsRequestDomain opamp) {

    public record OpampConnectionSettingsRequestDomain(
            CertificateRequestDomain certificateRequest
    ) {

        public record CertificateRequestDomain(byte[] csr) {

        }
    }
}
