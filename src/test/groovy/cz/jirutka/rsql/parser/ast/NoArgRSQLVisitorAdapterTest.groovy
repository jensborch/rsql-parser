/*
 * The MIT License
 *
 * Copyright 2013-2014 Jakub Jirutka <jakub@jirutka.cz>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package cz.jirutka.rsql.parser.ast

import static cz.jirutka.rsql.parser.ast.RSQLOperators.EQUAL
import cz.jirutka.rsql.parser.RSQLParser
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class NoArgRSQLVisitorAdapterTest extends Specification {

    def 'delegate visit(#className, Void) to visit(#className)'() {
        setup:
            def node = LogicalNode.isAssignableFrom(nodeClass) ?
                    nodeClass.newInstance([]) :
                    nodeClass.newInstance(RSQLOperators.EQUAL, 'sel', new StringArguments('arg'))
        and:
            def adapter = Spy(NoArgRSQLVisitorAdapter) {
                visit(_) >> null
            }
        when:
            adapter.visit(node, null)
        then:
            1 * adapter.visit({ nodeClass.isInstance(it) }) >> null
        where:
            nodeClass << [AndNode, OrNode, ComparisonNode]
            className = nodeClass.simpleName
    }

    def 'using parser with custom nested operators should have corret nesting level'() {
        setup:
            def level = 0
            def nestedOperator = new ComparisonOperator('=nested=', ComparisonOperator.NESTED_TYPE)
            def adapter = new NoArgRSQLVisitorAdapter<Void>() {

                def Void visit(AndNode nodes) {
                    nodes.stream().map(n -> n.accept(this))
                }

                def Void visit(OrNode nodes) {
                    nodes.stream().map(n -> n.accept(this))
                }

                def Void visit(ComparisonNode node) {
                    def args = node.getArgumentsObject()
                    if (args instanceof NestedArguments) {
                        level = args.asNode().nestingLevel
                        args.asNode().accept(this)
                    }
                }
            }
            def parser = new RSQLParser([EQUAL, nestedOperator] as Set)
        when:
            parser.parse('f1=nested=(f2=nested=(test==true))').accept(adapter)
        then:
            level == 1
    }
}
