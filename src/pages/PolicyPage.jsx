import React from 'react';
import './PolicyPage.scss';
import { Link } from 'react-router-dom';

const PolicyPage = () => {
  return (
    <div className="policy-page">
      <h1>Policy</h1>

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
          For data-related inquiries, contact support@spaik.ai.
        </p>
      </section>

      <section>
        <h2>5. Consent</h2>
        <p>
          <Link to="/terms" className="terms-link">Terms</Link>
          <Link to="/policy" className="terms-link">Privacy Policy</Link>

        </p>
      </section>
    </div>
  );
};

export default PolicyPage;
