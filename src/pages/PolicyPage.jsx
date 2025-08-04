import React from 'react';
import './PolicyPage.scss';

const PolicyPage = () => {
  return (
    <div className="policy-page">
      <div className="policy-page-wrapper">
        <div className="policy-content">
          <h1>Privacy Policy</h1>

          <section>
            <h2>1. Data Collection</h2>
            <p>
              We collect video and audio data uploaded by users solely for the purpose of speech and behavioral analysis.
              Additional data such as feedback scores, timestamps, and user interactions may also be stored.
            </p>
          </section>

          <section>
            <h2>2. Data Usage</h2>
            <p>
              The collected data is used to provide personalized feedback, improve AI accuracy, and enhance user experience.
              We do not sell or share personal data with third parties without consent.
            </p>
          </section>

          <section>
            <h2>3. Data Security</h2>
            <p>
              spAIk uses encryption and secure storage to protect user data. Access is restricted to authorized personnel only.
            </p>
          </section>

          <section>
            <h2>4. User Rights</h2>
            <p>
              Users may request access to, correction of, or deletion of their data at any time.
              For data-related inquiries, contact argentso22@gmail.com
            </p>
          </section>

          <section>
            <h2>5. Consent</h2>
            <p>
              By using our service, you agree to our Terms and Privacy Policy.
            </p>
          </section>
        </div>
      </div>
    </div>
  );
};

export default PolicyPage;
