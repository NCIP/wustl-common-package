/**
 * <p>Title: SendEmail Class>
 * <p>Description:	This Class is used to send emails.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 18, 2005
 */

package edu.wustl.common.util.global;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import edu.wustl.common.util.logger.Logger;

/**
 * This Class is used to send emails.
 *
 * @author gautam_shetty
 */
public class SendEmail
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(SendEmail.class);

	/**
	 * Used to send the mail with given parameters.
	 *
	 * @param to
	 *            "To" Address for sending the mail
	 * @param from
	 *            "From" Address for sending the mail
	 * @param host
	 *            "Host" from where to send the mail
	 * @param subject
	 *            "Subject" of the mail
	 * @param body
	 *            "Body" of the mail
	 * @return true if mail was successfully sent, false if it fails
	 */
	public boolean sendmail(String to, String from, String host, String subject, String body)
	{
		return sendmail(to, null, null, from, host, subject, body);
	}

	/**
	 * Used to send the mail with given parameters.
	 * Modified by kiran_pinnamaneni. code reviewer abhijit_naik
	 * @param to
	 *            "To" Address for sending the mail
	 * @param cc
	 *            "CC" Address for sending the mail
	 * @param bcc
	 *            "BCC" Address for sending the mail
	 * @param from
	 *            "From" Address for sending the mail
	 * @param host
	 *            "Host" from where to send the mail
	 * @param subject
	 *            "Subject" of the mail
	 * @param body
	 *            "Body" of the mail
	 * @return true if mail was successfully sent, false if it fails
	 */

	public boolean sendmail(String to, String cc, String bcc, String from, String host,
			String subject, String body)
	{

		String[] toArray = {to};
		String[] ccArray = null;
		String[] bccArray = null;
		if (cc != null)
		{
			ccArray = new String[1];
			ccArray[0] = cc;
		}
		if (bcc != null)
		{
			bccArray = new String[1];
			bccArray[0] = bcc;
		}
		return sendmail(toArray, ccArray, bccArray, from, host, subject, body);
	}

	/**
	 * Used to send the mail with given parameters.
	 *
	 * @param to
	 *            "To" List of address for sending the mail
	 * @param cc
	 *            "CC" List of address for sending the mail
	 * @param bcc
	 *            "BCC" List of address for sending the mail
	 * @param from
	 *            "From" Address for sending the mail
	 * @param host
	 *            "Host" from where to send the mail
	 * @param subject
	 *            "Subject" of the mail
	 * @param body
	 *            "Body" of the mail
	 * @return true if mail was successfully sent, false if it fails
	 */
	public boolean sendmail(String[] to, String[] cc, String[] bcc, String from, String host,
			String subject, String body)
	{

		// create some properties and get the default Session
		Properties props = new Properties();
		props.put("mail.smtp.host", host);

		Session session = Session.getDefaultInstance(props, null);
		session.setDebug(false);
		boolean sendStatus = true;

		try
		{
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));
			InternetAddress[] toAddress = convertArrayToInternetAddrArray(to);

			if (cc != null)
			{
				InternetAddress[] ccAddress = convertArrayToInternetAddrArray(cc);
				msg.setRecipients(Message.RecipientType.CC, ccAddress);
			}

			if (bcc != null)
			{
				InternetAddress[] bccAddress = convertArrayToInternetAddrArray(bcc);
				msg.setRecipients(Message.RecipientType.BCC, bccAddress);
			}
			msg.setRecipients(Message.RecipientType.TO, toAddress);
			msg.setSubject(subject);
			msg.setSentDate(new Date());
			// create and fill the first message part
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(body);
			Multipart mp = new MimeMultipart();
			mp.addBodyPart(messageBodyPart);
			// add the Multipart to the message
			msg.setContent(mp);
			Transport.send(msg);
		}
		catch (MessagingException mex)
		{
			logger.warn("Unable to send mail to: " + to,mex);
			Exception ex = null;
			if ((mex.getNextException()) != null)
			{
				ex = mex.getNextException();
				logger.warn("Exception= " + ex.getMessage());
			}
			sendStatus = false;
		}
		catch (Exception exception)
		{
			logger.warn("Unable to send mail to: " + to,exception);
			sendStatus = false;
		}
		return sendStatus;
	}

	/**
	 * This method convert Array To InternetAddrArray.
	 * Added by kiran_pinnamaneni. code reviewer abhijit_naik
	 * @param arrayToConvert convert int internet address array
	 * @return internetAddress
	 * @throws AddressException throws address exception
	 */
	private InternetAddress[] convertArrayToInternetAddrArray(String[] arrayToConvert)
			throws AddressException

	{
		InternetAddress[] internetAddress = new InternetAddress[arrayToConvert.length];

		for (int i = 0; i < arrayToConvert.length; i++)
		{
			internetAddress[i] = new InternetAddress(arrayToConvert[i]);
		}
		return internetAddress;
	}

}