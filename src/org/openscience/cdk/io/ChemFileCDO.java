/*
 * $RCSfile$
 * $Author$
 * $Date$
 * $Revision$
 * 
 * Copyright (C) 1997-2002  The Chemistry Development Kit (CDK) project
 * 
 * Contact: steinbeck@ice.mpg.de, gezelter@maul.chem.nd.edu, egonw@sci.kun.nl
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA. 
 */
package org.openscience.cdk.io;

import org.openscience.cdk.*;
import org.openscience.cdk.io.cml.*;
import org.openscience.cdk.io.cml.cdopi.*;
import java.util.*;

/**
 * CDO object needed as interface with the JCFL library for reading CML
 * encoded data.
 */ 
public class ChemFileCDO extends ChemFile implements CDOInterface {

    private AtomContainer currentMolecule;
    private SetOfMolecules currentSetOfMolecules;
    private ChemModel currentChemModel;
    private ChemSequence currentChemSequence;
    private Atom currentAtom;
    private Crystal currentCrystal;

    private Hashtable atomEnumeration;

    private int numberOfAtoms = 0;

    private int bond_a1;
    private int bond_a2;
    private int bond_order;
    private int bond_stereo;
    
    private double crystal_axis_x;
    private double crystal_axis_y;
    private double crystal_axis_z;

    protected org.openscience.cdk.tools.LoggingTool logger;

    /**
     * Basic contructor
     */
    public ChemFileCDO() {
      logger = new org.openscience.cdk.tools.LoggingTool(
                     this.getClass().getName() );
      currentChemSequence = new ChemSequence();
      currentChemModel = new ChemModel();
      currentSetOfMolecules = new SetOfMolecules();
      currentMolecule = new Molecule();
      atomEnumeration = new Hashtable();
    }

    // procedures required by CDOInterface

    /**
     * Procedure required by the CDOInterface. This function is only
     * supposed to be called by the JCFL library
     */
    public void startDocument() {
      logger.info("New CDO Object");
      currentChemSequence = new ChemSequence();
      currentChemModel = new ChemModel();
      currentSetOfMolecules = new SetOfMolecules();
      currentMolecule = new Molecule();
      atomEnumeration = new Hashtable();
    };

    /**
     * Procedure required by the CDOInterface. This function is only
     * supposed to be called by the JCFL library
     */
    public void endDocument() {
        if (getChemSequenceCount() == 0) {
            // assume there is one non-animation ChemSequence
            addChemSequence(currentChemSequence);
            logger.info("This file has " + getChemSequenceCount() + " sequence(s).");
        }
        logger.info("End CDO Object");
    };

    /**
     * Procedure required by the CDOInterface. This function is only
     * supposed to be called by the JCFL library
     */
    public void setDocumentProperty(String type, String value) {};

    /**
     * Procedure required by the CDOInterface. This function is only
     * supposed to be called by the JCFL library
     */
    public void startObject(String objectType) {
      logger.debug("START:" + objectType);
      if (objectType.equals("Molecule")) {
        currentChemModel = new ChemModel();
        currentSetOfMolecules = new SetOfMolecules();
        currentMolecule = new Molecule();
      } else if (objectType.equals("Atom")) {
        currentAtom = new Atom("H");
        logger.debug("Atom # " + numberOfAtoms);
        numberOfAtoms++;
      } else if (objectType.equals("Bond")) {
      } else if (objectType.equals("Animation")) {
        currentChemSequence = new ChemSequence();
      } else if (objectType.equals("Frame")) {
      } else if (objectType.equals("Crystal")) {
        currentMolecule = new Crystal(currentMolecule);
      } else if (objectType.equals("a-axis") ||
                 objectType.equals("b-axis") ||
                 objectType.equals("c-axis")) {
          crystal_axis_x = 0.0;
          crystal_axis_y = 0.0;
          crystal_axis_z = 0.0;
      }
    };

    /**
     * Procedure required by the CDOInterface. This function is only
     * supposed to be called by the JCFL library
     */
    public void endObject(String objectType) {
        logger.debug("END: " + objectType);
        if (objectType.equals("Molecule")) {
            if (currentMolecule instanceof Molecule) {
                currentSetOfMolecules.addMolecule((Molecule)currentMolecule);
                currentChemModel.setSetOfMolecules(currentSetOfMolecules);
            } else if (currentMolecule instanceof Crystal) {
                logger.debug("Adding crystal to chemModel");
                currentChemModel.setCrystal((Crystal)currentMolecule);
            }
            currentChemSequence.addChemModel(currentChemModel);
        } else if (objectType.equals("Frame")) {
        } else if (objectType.equals("Animation")) {
            addChemSequence(currentChemSequence);
            logger.info("This file has " + getChemSequenceCount() + " sequence(s).");
        } else if (objectType.equals("Atom")) {
            currentMolecule.addAtom(currentAtom);
        } else if (objectType.equals("Bond")) {
            logger.debug("Bond: " + bond_a1 + ", " + bond_a2 + ", " + bond_order);
            currentMolecule.addBond(bond_a1, bond_a2, bond_order);
        } else if (objectType.equals("a-axis")) {
          // set these variables
          if (currentMolecule instanceof Crystal) {
              Crystal current = (Crystal)currentMolecule;
              current.setA(crystal_axis_x,
                           crystal_axis_y,
                           crystal_axis_z);
          }
        } else if (objectType.equals("b-axis")) {
          if (currentMolecule instanceof Crystal) {
              Crystal current = (Crystal)currentMolecule;
              current.setB(crystal_axis_x,
                           crystal_axis_y,
                           crystal_axis_z);
          }
        } else if (objectType.equals("c-axis")) {
          if (currentMolecule instanceof Crystal) {
              Crystal current = (Crystal)currentMolecule;
              current.setC(crystal_axis_x,
                           crystal_axis_y,
                           crystal_axis_z);
          }
        }
    };

    /**
     * Procedure required by the CDOInterface. This function is only
     * supposed to be called by the JCFL library
     */
    public void setObjectProperty(String objectType, String propertyType,
                                  String propertyValue) {
      logger.debug("objectType: " + objectType);
      logger.debug("propType: " + propertyType);
      logger.debug("property: " + propertyValue);
      if (objectType.equals("Atom")) {
        if (propertyType.equals("type")) {
          currentAtom.setSymbol(propertyValue);
        } else if (propertyType.equals("x2")) {
          currentAtom.setX2D(new Double(propertyValue).doubleValue());
        } else if (propertyType.equals("y2")) {
          currentAtom.setY2D(new Double(propertyValue).doubleValue());
        } else if (propertyType.equals("x3")) {
          currentAtom.setX3D(new Double(propertyValue).doubleValue());
        } else if (propertyType.equals("y3")) {
          currentAtom.setY3D(new Double(propertyValue).doubleValue());
        } else if (propertyType.equals("z3")) {
          currentAtom.setZ3D(new Double(propertyValue).doubleValue());
        } else if (propertyType.equals("id")) {
          logger.debug("id" + propertyValue);
          atomEnumeration.put(propertyValue, new Integer(numberOfAtoms));
        }
      } else if (objectType.equals("Bond")) {
        if (propertyType.equals("atom1")) {
          bond_a1 = new Integer(propertyValue).intValue();
        } else if (propertyType.equals("atom2")) {
          bond_a2 = new Integer(propertyValue).intValue();
        } else if (propertyType.equals("order")) {
          try {
            bond_order = new Integer(propertyValue).intValue();
          } catch (Exception e) {
            logger.error("Cannot convert to int: " + propertyValue);
            bond_order = 1;
          }
        }
      } else if (objectType.equals("Crystal")) {
          // set these variables
          if (currentMolecule instanceof Crystal) {
              Crystal current = (Crystal)currentMolecule;
              if (propertyType.equals("spacegroup")) {
                  current.setSpaceGroup(propertyValue);
              }
          } else {
              logger.warn("Cannot add crystal cell parameters to a non " +
                           "Crystal class!");
          }
      } else if (objectType.equals("a-axis") ||
                 objectType.equals("b-axis") ||
                 objectType.equals("c-axis")) {
          // set these variables
          if (currentMolecule instanceof Crystal) {
              Crystal current = (Crystal)currentMolecule;
              if (propertyType.equals("x")) {
                  crystal_axis_x = Double.parseDouble(propertyValue);
              } else if (propertyType.equals("y")) {
                  crystal_axis_y = Double.parseDouble(propertyValue);
              } else if (propertyType.equals("z")) {
                  crystal_axis_z = Double.parseDouble(propertyValue);
              }
          } else {
              logger.warn("Cannot add crystal cell parameters to a non " +
                           "Crystal class!");
          }
      }
      logger.debug("Object property set...");
    };

    /**
     * Procedure required by the CDOInterface. This function is only
     * supposed to be called by the JCFL library
     */
    public CDOAcceptedObjects acceptObjects() {
        CDOAcceptedObjects objects = new CDOAcceptedObjects();
        objects.add("Molecule");
        objects.add("Fragment");
        objects.add("Atom");
        objects.add("Bond");
        objects.add("Animation");
        objects.add("Frame");
        objects.add("Crystal");
        objects.add("a-axis");
        objects.add("b-axis");
        objects.add("c-axis");
      return objects;
    };
}

