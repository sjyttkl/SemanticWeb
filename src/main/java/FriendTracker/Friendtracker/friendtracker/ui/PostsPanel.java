package FriendTracker.Friendtracker.friendtracker.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.semwebprogramming.friendtracker.model.Post;

/**
 *  A graphical component which displays a list of blog posts. 
 *
 */
public class PostsPanel extends JPanel implements ListSelectionListener {
	JList _postList;
	DefaultListModel _listModel;
	JScrollPane _scrollPane;
	
	JPanel _postDetailPanel;
	JPanel _titleDatePanel;
	JTextArea _postContentTextArea;
	JTextField _postTitleTextField;
	JTextField _postDateTextField;
	JScrollPane _contentScrollPane;
	
	public PostsPanel()
	{
		JPanel tempPanel;
		
		_listModel = new DefaultListModel();
		_postList = new JList(_listModel);
		_scrollPane = new JScrollPane(_postList);
		
		_postTitleTextField = new JTextField();
		_postDateTextField = new JTextField();
		_postContentTextArea = new JTextArea();
		_postContentTextArea.setWrapStyleWord(true);
		
		_titleDatePanel = new JPanel();
		_titleDatePanel.setLayout(new BoxLayout(_titleDatePanel, BoxLayout.Y_AXIS));
		
		
		tempPanel = new JPanel();
		tempPanel.setLayout(new BoxLayout(tempPanel, BoxLayout.X_AXIS));
		tempPanel.add(new JLabel("Title"));
		tempPanel.add(_postTitleTextField);
		
		_titleDatePanel.add(tempPanel);
		
		tempPanel = new JPanel();
		tempPanel.setLayout(new BoxLayout(tempPanel, BoxLayout.X_AXIS));
		tempPanel.add(new JLabel("Date"));
		tempPanel.add(_postDateTextField);
		
		_titleDatePanel.add(tempPanel);
		
		_postDetailPanel = new JPanel();
		_postDetailPanel.setLayout(new BoxLayout(_postDetailPanel, BoxLayout.Y_AXIS));
		_postDetailPanel.add(_titleDatePanel);
		_contentScrollPane = new JScrollPane(_postContentTextArea);
		_postDetailPanel.add(_contentScrollPane);
	}
	
	public void initialize()
	{
		_postList.addListSelectionListener(this);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(_scrollPane);
		add(_postDetailPanel);
	}
	

	public void valueChanged(ListSelectionEvent lse) {
		Post selectedPost = null;
		
		if(!lse.getValueIsAdjusting())
		{
			for(int i = lse.getFirstIndex(); i <= lse.getLastIndex(); i++)
			{
				if(_postList.isSelectedIndex(i))
				{
					selectedPost = (Post) _listModel.getElementAt(i);
					break;
				}
			}
			
			if(null != selectedPost)
			{
				_postTitleTextField.setText(selectedPost.getTitle());
				if(null != selectedPost.getPostDate())
				{
					_postDateTextField.setText(selectedPost.getPostDate().toString());	
				}
				else
				{
					_postDateTextField.setText("Post date unavailable.");
				}
				_postContentTextArea.setText(selectedPost.getContent());
			}
			else
			{
				_postTitleTextField.setText("");
				_postDateTextField.setText("");
				_postContentTextArea.setText("");
			}
			
			repaint();
		}
	}

	
	/**
	 * Set the list of posts that should appear in this component.
	 * 
	 * @param posts the posts that should appear in this component.
	 */
	public void setPosts(List<Post> posts)
	{
		_listModel.clear();
		for(Post p : posts)
		{
			_listModel.addElement(p);
		}
	}
}
