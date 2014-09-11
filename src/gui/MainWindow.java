/******************************************************************************
 * JAlarm
 * @author  : Christophe De Troyer
 * Last edit: 11-sep-2014 19:39:13                                                   
 * Full source can be found on GitHub      :
 * https://github.com/m1dnight/JAlarm                                
 ******************************************************************************/
package gui;


public class MainWindow
{
	public static void main(String[] args)
	{
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AlarmFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AlarmFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AlarmFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AlarmFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				final AlarmFrame mainFrame = new AlarmFrame();
				mainFrame.setVisible(true);
				

			}
		});
	}
}