// src/pages/Auth.jsx
import { useState } from 'react';
import {
  Box,
  Button,
  Container,
  FormControl,
  FormLabel,
  Input,
  Stack,
  Text,
  Heading,
  InputGroup,
  InputRightElement,
  FormErrorMessage,
  useToast,
  useColorMode,
} from '@chakra-ui/react';
import { ViewIcon, ViewOffIcon } from '@chakra-ui/icons';

const Auth = () => {
  const [isLogin, setIsLogin] = useState(true);
  const [showPassword, setShowPassword] = useState(false);
  const { colorMode } = useColorMode();
  const toast = useToast();

  // Form states
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    confirmPassword: '',
  });

  // Error states
  const [errors, setErrors] = useState({
    email: '',
    password: '',
    confirmPassword: '',
  });

  const validateForm = () => {
    const newErrors = {};
    
    // Email validation
    if (!formData.email) {
      newErrors.email = 'Email is required';
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = 'Invalid email address';
    }

    // Password validation
    if (!formData.password) {
      newErrors.password = 'Password is required';
    } else if (formData.password.length < 6) {
      newErrors.password = 'Password must be at least 6 characters';
    }

    // Confirm password validation (for signup only)
    if (!isLogin && formData.password !== formData.confirmPassword) {
      newErrors.confirmPassword = 'Passwords do not match';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    
    if (validateForm()) {
      toast({
        title: isLogin ? 'Login Successful' : 'Account Created',
        description: isLogin 
          ? 'Welcome back to Todoer!'
          : 'Welcome to Todoer! Please check your email to verify your account.',
        status: 'success',
        duration: 5000,
        isClosable: true,
      });
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    // Clear error when user starts typing
    if (errors[name]) {
      setErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }
  };

  return (
    <Box minH="calc(100vh - 80px)" py={10}>
      <Container maxW="lg">
        <Stack spacing={8} mx="auto" maxW="lg" py={12} px={6}>
          <Stack align="center">
            <Heading 
              fontSize="4xl" 
              bgGradient="linear(to-r, blue.400, purple.500)"
              bgClip="text"
            >
              {isLogin ? 'Welcome Back' : 'Join Todoer'}
            </Heading>
            <Text fontSize="lg" color="gray.600">
              {isLogin 
                ? 'to keep track of your tasks ✌️'
                : 'to start organizing your life ✨'}
            </Text>
          </Stack>

          <Box
            rounded="lg"
            bg={colorMode === 'light' ? 'white' : 'gray.700'}
            boxShadow="lg"
            p={8}
          >
            <form onSubmit={handleSubmit}>
              <Stack spacing={4}>
                <FormControl isInvalid={errors.email}>
                  <FormLabel>Email address</FormLabel>
                  <Input
                    name="email"
                    type="email"
                    value={formData.email}
                    onChange={handleInputChange}
                    autoComplete="email"
                  />
                  <FormErrorMessage>{errors.email}</FormErrorMessage>
                </FormControl>

                <FormControl isInvalid={errors.password}>
                  <FormLabel>Password</FormLabel>
                  <InputGroup>
                    <Input
                      name="password"
                      type={showPassword ? 'text' : 'password'}
                      value={formData.password}
                      onChange={handleInputChange}
                      autoComplete={isLogin ? "current-password" : "new-password"}
                    />
                    <InputRightElement>
                      <Button
                        variant="ghost"
                        onClick={() => setShowPassword(s => !s)}
                        size="sm"
                      >
                        {showPassword ? <ViewOffIcon /> : <ViewIcon />}
                      </Button>
                    </InputRightElement>
                  </InputGroup>
                  <FormErrorMessage>{errors.password}</FormErrorMessage>
                </FormControl>

                {!isLogin && (
                  <FormControl isInvalid={errors.confirmPassword}>
                    <FormLabel>Confirm Password</FormLabel>
                    <InputGroup>
                      <Input
                        name="confirmPassword"
                        type={showPassword ? 'text' : 'password'}
                        value={formData.confirmPassword}
                        onChange={handleInputChange}
                        autoComplete="new-password"
                      />
                      <InputRightElement>
                        <Button
                          variant="ghost"
                          onClick={() => setShowPassword(s => !s)}
                          size="sm"
                        >
                          {showPassword ? <ViewOffIcon /> : <ViewIcon />}
                        </Button>
                      </InputRightElement>
                    </InputGroup>
                    <FormErrorMessage>{errors.confirmPassword}</FormErrorMessage>
                  </FormControl>
                )}

                <Stack spacing={10} pt={2}>
                  <Button
                    size="lg"
                    bg="blue.400"
                    color="white"
                    _hover={{
                      bg: 'blue.500',
                    }}
                    type="submit"
                  >
                    {isLogin ? 'Sign In' : 'Sign Up'}
                  </Button>
                </Stack>

                <Stack pt={6}>
                  <Text align="center">
                    {isLogin ? "Don't have an account?" : "Already have an account?"}{' '}
                    <Button
                      variant="link"
                      color="blue.400"
                      onClick={() => setIsLogin(!isLogin)}
                    >
                      {isLogin ? 'Sign Up' : 'Sign In'}
                    </Button>
                  </Text>
                </Stack>
              </Stack>
            </form>
          </Box>
        </Stack>
      </Container>
    </Box>
  );
};

export default Auth;