import React from "react";
import "./TermsPage.scss";

const TermsPage = () => {
  return (
    <div className="terms-page">
      <div className="terms-page-wrapper">
        <div className="terms-content">
          <h1>Terms of Service</h1>

          <section>
            <h2>1. Service Overview</h2>
            <p>
              spAIk is an AI-powered platform that provides real-time feedback on
              user-uploaded videos, analyzing elements such as speech speed, pitch,
              eye contact, facial expressions, body movement, and speech disfluency.
              The service is designed to enhance public speaking and interview
              performance.
            </p>
          </section>

          <section>
            <h2>2. User Responsibilities</h2>
            <ul>
              <li>
                Users must ensure that any uploaded content (video/audio) does not
                infringe on copyrights, privacy, or likeness rights of others.
              </li>
              <li>
                Content that includes hate speech, explicit material, harassment,
                or any unlawful activity is strictly prohibited.
              </li>
              <li>
                Users are responsible for maintaining the confidentiality of their
                account credentials and activity.
              </li>
            </ul>
          </section>

          <section>
            <h2>3. Service Limitations</h2>
            <ul>
              <li>
                spAIk may suspend or restrict access to the service if there is a
                violation of these terms or misuse of the platform.
              </li>
              <li>
                The service is provided “as is” and may be subject to occasional
                interruptions or updates.
              </li>
            </ul>
          </section>

          <section>
            <h2>4. Intellectual Property</h2>
            <p>
              All AI models, feedback tools, and analysis technology are the
              intellectual property of spAIk. Users may not reverse engineer,
              resell, or replicate the service without permission.
            </p>
          </section>
        </div>
      </div>
    </div>
  );
};

export default TermsPage;
