package ca.bcit.comp4656.assign2ws.util;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;


/**
 *  
 * @author Arash Ghavami
 *
 */
public class PropertiesUtil
{
	private static Logger logger = Logger.getLogger( PropertiesUtil.class );

	
	public static String getString( ResourceBundle resourceBundle, String key )
	{
		try
		{
			if ( System.getenv( key ) != null )
			{
				return System.getenv( key );
			}
			return resourceBundle.getString( key );
		}
		catch ( MissingResourceException e )
		{
			logger.error( System.out.format("Unable to find key %s]", key) );
			return '!' + key + '!';
		}
	}

	public static String getString( ResourceBundle resourceBundle,String key, Object... args )
	{
		try
		{
			if ( System.getenv( key ) != null )
			{
				return System.getenv( key );
			}
			String s = resourceBundle.getString( key );
			return MessageFormat.format( s, args );
		}
		catch ( MissingResourceException e )
		{
			logger.error( System.out.format("Unable to find key %s]", key) );
			return '!' + key + '!';
		}
	}

}
