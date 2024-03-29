package assignment2.FileSharingApp;


/**
* FileSharingApp/_FileSharingStub.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from FileSharing.idl
* Thursday, February 22, 2024 2:40:34 o'clock AM EST
*/


// Interface for P2P file sharing services
public class _FileSharingStub extends org.omg.CORBA.portable.ObjectImpl implements FileSharing
{


  // Registers a file with the server to be shared with others
  public void registerFile (String filename, String clientID, String clientAddress, String clientPort)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("registerFile", true);
                $out.write_string (filename);
                $out.write_string (clientID);
                $out.write_string (clientAddress);
                $out.write_string (clientPort);
                $in = _invoke ($out);
                return;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                registerFile (filename, clientID, clientAddress, clientPort        );
            } finally {
                _releaseReply ($in);
            }
  } // registerFile


  // Removes a file from the list of shared files
  public void removeFile (String filename, String clientID)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("removeFile", true);
                $out.write_string (filename);
                $out.write_string (clientID);
                $in = _invoke ($out);
                return;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                removeFile (filename, clientID        );
            } finally {
                _releaseReply ($in);
            }
  } // removeFile


  // Searches for a file by name and returns a list of files matching the query
  public String[] searchFile (String filename)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("searchFile", true);
                $out.write_string (filename);
                $in = _invoke ($out);
                String $result[] = assignment2.FileSharingApp.FileSharingPackage.FileInfoListHelper.read ($in);
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return searchFile (filename        );
            } finally {
                _releaseReply ($in);
            }
  } // searchFile


  // Retrieves the owner information for a specific file
  public String getFileOwner (String filename)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("getFileOwner", true);
                $out.write_string (filename);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return getFileOwner (filename        );
            } finally {
                _releaseReply ($in);
            }
  } // getFileOwner

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:FileSharingApp/FileSharing:1.0"};

  public String[] _ids ()
  {
    return (String[])__ids.clone ();
  }

  private void readObject (java.io.ObjectInputStream s) throws java.io.IOException
  {
     String str = s.readUTF ();
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     org.omg.CORBA.Object obj = orb.string_to_object (str);
     org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate ();
     _set_delegate (delegate);
   } finally {
     orb.destroy() ;
   }
  }

  private void writeObject (java.io.ObjectOutputStream s) throws java.io.IOException
  {
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     String str = orb.object_to_string (this);
     s.writeUTF (str);
   } finally {
     orb.destroy() ;
   }
  }
} // class _FileSharingStub
