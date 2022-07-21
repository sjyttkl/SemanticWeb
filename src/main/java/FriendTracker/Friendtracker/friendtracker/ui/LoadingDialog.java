package FriendTracker.Friendtracker.friendtracker.ui;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

/**
 *  Presents a progress bar to users to assure them that the 
 *  system is working while there is a pause in operation. 
 */
public class LoadingDialog extends JDialog {

	private String _message;
	private String _title;
	private JLabel _msgLabel;
	final JProgressBar _progress = new JProgressBar(0, 100);
	private Progressor _progressor;
	
	public LoadingDialog(String title, String msg)
	{
		_message = msg;
		_title = title;
		initUI();
	}

	private void initUI() {

		setTitle(_title);
		_msgLabel = new JLabel(_message);
		getContentPane().add(_msgLabel, BorderLayout.CENTER);
		getContentPane().add(_progress, BorderLayout.SOUTH);
		pack();
	}
	
	@Override
	public void setVisible(boolean shouldShow) {
		if(shouldShow)
		{
			if(null == _progressor)
			{
				_progressor = new Progressor();
				_progressor.addPropertyChangeListener(
					     new PropertyChangeListener() {
					         public  void propertyChange(PropertyChangeEvent evt) {
					             if ("progress".equals(evt.getPropertyName())) {
					                 _progress.setValue((Integer)evt.getNewValue());
					             }
					         }
					     });
	
				_progressor.execute();
			}
		}
		else
		{
			_progressor.stop();
		}
		
		super.setVisible(shouldShow);
	}
	
	final class Progressor extends SwingWorker<Object, Object>
	{
		boolean _shouldContinue = true;
		int _prog = 0;
		
		@Override
		protected Object doInBackground() throws Exception {
			while(_shouldContinue)
			{
				Thread.sleep(500);
				_prog++;
				setProgress(_prog % 100);
			}
			return null;
		}
		
		public void stop()
		{
			_shouldContinue = false;
		}
		
	}
	
}
