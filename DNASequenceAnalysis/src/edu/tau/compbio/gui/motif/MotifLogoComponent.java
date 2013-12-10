package edu.tau.compbio.gui.motif;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JComponent;

import org.biojava.bio.dist.Count;
import org.biojava.bio.dist.Distribution;
import org.biojava.bio.dist.DistributionTools;
import org.biojava.bio.dist.IndexedCount;
import org.biojava.bio.gui.DNAStyle;
import org.biojava.bio.gui.DistributionLogo;
import org.biojava.bio.gui.TextLogoPainter;
import org.biojava.bio.seq.DNATools;
import org.biojava.bio.symbol.AlphabetIndex;
import org.biojava.bio.symbol.AlphabetManager;
import org.biojava.bio.symbol.FiniteAlphabet;


/**
 * The MotifLogoComponent is a JComponent that displays one or more motif logos.
 * It's implemented using the biojava package.
 * 
 * @author Chaim Linhart
 */
@SuppressWarnings("serial")
public class MotifLogoComponent extends JComponent
{

    /**
     * Create an empty MotifLogoComponent.
     * Call addMotif to add motif logo(s).
     */
	public MotifLogoComponent ()
	{
	}	

	/**
	 * Set graphical attributes of the MotifLogoComponent.
	 * @param charWidth  width of each character in the logo
	 * @param charHeight height of each character in the logo
	 * @param bgColor    background color of logo
	 */
	public void setPreferences (int charWidth, int charHeight, Color bgColor)
	{
		m_charWidth = charWidth;
		m_charHeight = charHeight;
		m_bgColor = bgColor;
	}
	
    /**
     * Set the length of the motif(s) to be displayed.
     * @param length the length of the motif(s)
     */
    public void setMotifLength (int length) 
    {
    	m_motifLength = length;
    }
    
    /**
     * Reverse-complement the motif.
     */
    public void revCompMotif()
    				throws Exception
    {
    	try {
    		double[][] newWeights = new double[getComponentCount()][4];
    		for (int i=0; i < getComponentCount(); i++) {
	    		DistributionLogo dl = (DistributionLogo) getComponent(getComponentCount()-i-1);
	    		Distribution dist = dl.getDistribution();
	    		double wa = dist.getWeight(DNATools.a());
	    		double wc = dist.getWeight(DNATools.c());
	    		double wg = dist.getWeight(DNATools.g());
	    		double wt = dist.getWeight(DNATools.t());
	    		newWeights[i][0] = wt;
	    		newWeights[i][1] = wg;
	    		newWeights[i][2] = wc;
	    		newWeights[i][3] = wa;
	    			//Dists = DistributionTools.new Distribution(dist.setWeight(DNATools.a(), wt);
	    		//dist.setWeight(DNATools.c(), wg);
//	    		dist.setWeight(DNATools.g(), wc);
//	    		dist.setWeight(DNATools.t(), wa);
//	    		dl.setDistribution(dist);
	    	}
    		for (int i=0; i < getComponentCount(); i++) {
	    		DistributionLogo dl = (DistributionLogo) getComponent(i);
	    		Distribution dist = dl.getDistribution();
	    		dist.setWeight(DNATools.a(), newWeights[i][0]);
	    		dist.setWeight(DNATools.c(), newWeights[i][1]);
	    		dist.setWeight(DNATools.g(), newWeights[i][2]);
	    		dist.setWeight(DNATools.t(), newWeights[i][3]);
    		}
		} catch (Exception ex) {
			throw new Exception ("Problems creating rev-comp distribution logo: " + ex.getLocalizedMessage());
		}
    	validate();
    	repaint();
    }
    
    /**
     * Add a consensus (degenerate string) motif to display.
     * @param motif the motif to display. motif[i] is the set of bases at position i (0<=i<L).
     *              The set of bases must be a subset of { "A", "C", "G", "T" }.
     *              The length of the motif should be the same as set by setMotifLength() (if set).
     */
    public void addMotif (String[] motif)
    {
    	if (m_motifLength == 0) {
    		m_motifLength = motif.length;
    	}
    	else if (motif.length != m_motifLength) {
			throw new RuntimeException("Inconsistent motif length: " + motif.length + " != " + m_motifLength);
    	}
    	
    	// Create probabilities matrix
    	double[][] mat = new double[motif.length][4];
        for (int pos=0; pos < motif.length; pos++) {
	        // Initialize column
        	for (int b=0; b < 4; b++) {
        		mat[pos][b] = 0.0;
        	}
        	// Compute uniform probabilities to all specified bases
        	int nBases = motif[pos].length();
        	for (int b=0; b < nBases; b++) {
        		if (motif[pos].charAt(b) == 'A') {
        			mat[pos][0] += 1.0/nBases;
        		}
        		else if (motif[pos].charAt(b) == 'C') {
        			mat[pos][1] += 1.0/nBases;
        		}
        		else if (motif[pos].charAt(b) == 'G') {
        			mat[pos][2] += 1.0/nBases;
        		}
        		else if (motif[pos].charAt(b) == 'T') {
        			mat[pos][3] += 1.0/nBases;
        		}
        		else {
        			throw new RuntimeException("Illegal base at position " + pos + " in motif: " + motif[pos].charAt(b));
        		}
        	}
        }
        
        try {
        	addMotif (mat);
        } 
        catch (Exception e) {
        	throw new RuntimeException ("Problems displaying motif logo: " + e.getLocalizedMessage());
        }
    }

    /**
     * Add a motif to display.
     * @param motif the motif to display. motif[i][j] is the probability (or weight)
     *              of the j'th base (0=A,1=C,2=G,3=T) at position i (0<=i<L).
     *              The length of the motif should be the same as set by setMotifLength().
     * @throws Exception 
     */
    public void addMotif (double[][] motif)
    				throws Exception
    {
    	//m_numMotifs ++;
    	m_numMotifs = 1;
    	//getContentPane().setLayout (new GridLayout (m_numMotifs, m_motifLength));
		setLayout (new GridLayout (m_numMotifs, 17/*m_motifLength*/));
		setBackground (m_bgColor);

    	// Add logo of given motif 
    	try {
	        for (int pos=0; pos < motif.length; pos++) {
	            Distribution dist = createDistribution (motif[pos][0],motif[pos][1],motif[pos][2],motif[pos][3]);
	            DistributionLogo dl = new DistributionLogo();
	            //dl.setRenderingHints(hints);
	            dl.setBackground (m_bgColor);
	            dl.setOpaque (true);
	            dl.setDistribution (dist);
	            dl.setPreferredSize (new Dimension(m_charWidth, m_charHeight));
	            TextLogoPainter lPnt = new TextLogoPainter();
	            lPnt.setLogoFont(LOGO_FONT);
	            dl.setLogoPainter (lPnt);
	            DNAStyle style = new DNAStyle();
	            style.setOutlinePaint(DNATools.t(), T_BASE_COLOR);
	            style.setFillPaint(DNATools.t(), T_BASE_COLOR);
	            style.setOutlinePaint(DNATools.a(), A_BASE_COLOR);
	            style.setFillPaint(DNATools.a(), A_BASE_COLOR);
	            style.setOutlinePaint(DNATools.g(), G_BASE_COLOR);
	            style.setFillPaint(DNATools.g(), G_BASE_COLOR);
	            style.setOutlinePaint(DNATools.c(), C_BASE_COLOR);
	            style.setFillPaint(DNATools.c(), C_BASE_COLOR);
	         
	            dl.setStyle (style);
				add (dl);
	        }
		} catch (Exception ex) {
			throw new Exception ("Problems creating distribution logo: " + ex.getLocalizedMessage());
		}
    }
    
    // Create a Distribution class from a given set of DNA probabilities 
    private static Distribution createDistribution (double pA, double pC, double pG, double pT) 
    				throws Exception
    {
    	FiniteAlphabet alpha = DNATools.getDNA();
    	@SuppressWarnings("unused")
		AlphabetIndex index = AlphabetManager.getAlphabetIndex(alpha);
    	Distribution dist = null;
    	
    	try {
    		// Make a Count (we add a small value to each cell, since the logo
    		// can't be painted if there are 0's)
    		Count cnt = new IndexedCount(alpha);
    		cnt.increaseCount (DNATools.a(), 100.0*pA+1.0);
    		cnt.increaseCount (DNATools.c(), 100.0*pC+1.0);
    		cnt.increaseCount (DNATools.g(), 100.0*pG+1.0);
    		cnt.increaseCount (DNATools.t(), 100.0*pT+1.0);

    		//System.out.println("COUNT");
    		//for (int i = 0; i < alpha.size(); i++) {
    		//	AtomicSymbol s = (AtomicSymbol) index.symbolForIndex(i);
    		//	System.out.println (s.getName() + " : " + c.getCount(s));
    		//}
    		
    		// Make Distribution
    		dist = DistributionTools.countToDistribution (cnt);
    		
    		//System.out.println("\nDISTRIBUTION");
    		//for (int i = 0; i < alpha.size(); i++) {
    		//	Symbol s = index.symbolForIndex(i);
    		//	System.out.println(s.getName()+" : "+dist.getWeight(s));
    		//}
    	}
    	catch (Exception ex) {
    		throw new Exception ("Problems creating distribution: " + ex.getLocalizedMessage());
    	}
    	
    	return (dist);
    }


    // The Container that contains the logos
    protected Container m_container = null;
    
    // Length of each motif
    protected int m_motifLength = 0;

    // Number of motifs
    protected int m_numMotifs = 0;
    
    // Graphical attributes
	protected int   m_charWidth  = 50;
	protected int   m_charHeight = 50;
	protected Color m_bgColor    = Color.WHITE;

	protected static final Color T_BASE_COLOR = new Color(0xcc0000);
	protected static final Color A_BASE_COLOR = new Color(0x00cc00);
	protected static final Color G_BASE_COLOR = new Color(0xffb300);
	protected static final Color C_BASE_COLOR = new Color(0x0000cc);
	protected static final Font  LOGO_FONT    = new Font("Verdana", Font.PLAIN, 50);

}

