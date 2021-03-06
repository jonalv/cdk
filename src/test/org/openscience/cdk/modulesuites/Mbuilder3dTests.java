/* $RCSfile$    
 * $Author: egonw $    
 * $Date: 2006-03-30 00:42:34 +0200 (Thu, 30 Mar 2006) $    
 * $Revision: 5865 $
 * 
 * Copyright (C) 1997-2007  The Chemistry Development Kit (CDK) project
 * 
 * Contact: cdk-devel@lists.sourceforge.net
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
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA. 
 */

package org.openscience.cdk.modulesuites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.openscience.cdk.coverage.Builder3dCoverageTest;
import org.openscience.cdk.modeling.builder3d.ModelBuilder3DTest;
import org.openscience.cdk.modeling.builder3d.TemplateHandler3DTest;
import org.openscience.cdk.qsar.descriptors.atomic.PartialTChargeMMFF94DescriptorTest;

/**
 * TestSuite that runs all the sample tests.
 *
 * @cdk.module test-builder3d
 */
@RunWith(value=Suite.class)
@SuiteClasses(value={
    Builder3dCoverageTest.class,
    ModelBuilder3DTest.class,
    PartialTChargeMMFF94DescriptorTest.class,
    TemplateHandler3DTest.class
})
public class Mbuilder3dTests {}
