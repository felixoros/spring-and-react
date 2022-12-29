import React, { useMemo } from 'react';
import "./Flex.css";

type FlexDirection = 'row' | 'col';

interface IFlexProps {
    children?: React.ReactNode;
    direction: FlexDirection;
    classNames?: string;
    style?: React.CSSProperties | undefined;
}

const Flex = ({children, direction, classNames, style}: IFlexProps): JSX.Element => {
    const flexDirectionClass = 
        useMemo(() => direction === 'row' ? 'flex-row' : 'flex-col', [direction]);

    const additionalClassNames = 
        useMemo(() => classNames ? classNames : '', [classNames]);

    return (
        <div 
            className={`flex ${flexDirectionClass} ${additionalClassNames}`} 
            style={style}>
            {children}
        </div>
    );
}

export default Flex