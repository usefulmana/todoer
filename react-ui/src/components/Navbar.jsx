// src/components/Navbar.jsx
import { useNavigate } from 'react-router-dom';
import {
  Flex,
  Heading,
  Button,
  IconButton,
  HStack,
  useColorMode,
} from '@chakra-ui/react';
import { MoonIcon, SunIcon } from '@chakra-ui/icons';

const Navbar = () => {
  const { colorMode, toggleColorMode } = useColorMode();
  const navigate = useNavigate();

  return (
    <Flex
      as="nav"
      align="center"
      justify="space-between"
      wrap="wrap"
      padding={6}
      bg={colorMode === 'light' ? 'white' : 'gray.800'}
      borderBottom="1px"
      borderColor={colorMode === 'light' ? 'gray.200' : 'gray.700'}
    >
      <Heading 
        as="h1" 
        size="lg" 
        letterSpacing="tight" 
        cursor="pointer"
        onClick={() => navigate('/')}
      >
        Todoer
      </Heading>
      <HStack spacing={4}>
        <IconButton
          icon={colorMode === 'light' ? <MoonIcon /> : <SunIcon />}
          onClick={toggleColorMode}
          variant="ghost"
          aria-label="Toggle dark mode"
        />
        <Button 
          variant="outline" 
          colorScheme="blue" 
          onClick={() => navigate('/auth')}
        >
          Sign In
        </Button>
      </HStack>
    </Flex>
  );
};

export default Navbar;