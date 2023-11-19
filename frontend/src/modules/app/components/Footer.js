import {FormattedMessage} from 'react-intl';
import './FooterStyle.css';

const Footer = () => (

    <div className="footer border bg-secondary">
        <footer className="text-footer">
            <p className="text-center text-light">
                <FormattedMessage id="project.app.Footer.text"/>
            </p>
        </footer>
    </div>

);

export default Footer;
